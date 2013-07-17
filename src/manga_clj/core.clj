(ns manga-clj.core
  (:use [clojure.test :only [is are]]
        [clojure.string :only [trim join]]
        [clojure.pprint :only [pprint]]
        [clojure.tools.logging :only [info warn debug]]
        [clj-logging-config.log4j])
  (:require [net.cgrand.enlive-html :as html]
            [clojure.java.io :as io]
            [clojure.tools.cli :as c])
  (:import [javax.imageio ImageIO]
           [java.net URL]))

(set-logger! :pattern "%m%n" :level :warn)

(def BASE_URL "http://www.mangareader.net")

(defn normalize-name [name]
  (-> name (.trim) (.replaceAll "\\s+" "-")))

(defn parse-query [query]
  (if-let [[name episode-nr] (rest (first (re-seq #"\s*(.*?)(\d+)\s*$" query)))]
    [(normalize-name name) episode-nr]
    (throw (Exception. "Failed to parse query"))))

(defn compose-url [name episode-nr]
  (format "%s/%s/%s" BASE_URL name episode-nr))

(defn ->absolute [root uri]
  (if (.startsWith uri "http:")
    uri
    (str root uri)))

(defn- as-absolute [uri]
  (->absolute BASE_URL uri))

(defn get-image-name [uri]
  (-> (re-seq #"([^\\/]+?)\.(\w+)$" (str uri)) first rest))

(are [expected actual] (= expected actual)
     "x-y-z" (normalize-name "  x   y   z  ")
     ["naruto" "100"] (parse-query " naruto  100  ")
     ["first-flight" "100"] (parse-query " first flight  100  ")
     "http://www.mangareader.net/naruto/100" (compose-url "naruto" "100")
     "http://x.com/b/a" (->absolute "http://x.com" "/b/a")
     "http://x.com/b/a" (->absolute "http://x.com" "http://x.com/b/a")
     )

(defn fetch-url [url]
  (html/html-resource (URL. url)))

(defn parse-page-urls [source]
  (for [option-el (html/select source [:select#pageMenu :option])]
    (get-in option-el [:attrs :value])))

(defn parse-img-url [source]
  (let [img-el (first (html/select source [:img#img]))]
    (get-in img-el [:attrs :src])))

(defn download-image
  "@types: str -> tuple[str, java.awt.image.BufferedImage]"
  [page-url]
  (debug "Download image from: " page-url)
  (when-let [img-url (parse-img-url (fetch-url page-url))]
    [img-url (ImageIO/read (URL. img-url))]))

(defn prepare-dest-folder [name]
  (let [f (io/file name)]
    (if-not (.exists f)
      (.mkdirs f))))

(defn get-file-size-in-kb [file]
  (int (/ (.length file) 1024.0)))

(defn store-img [dest-folder [url img]]
  (when img
    (let [[file-name ext] (get-image-name url)
          file-name-ext (format "%s.%s" file-name ext)
          dest-file (io/file (join "/" [dest-folder file-name-ext]))]
      (ImageIO/write img ext dest-file)
      (info "Stored: " (.getName dest-file) (get-file-size-in-kb dest-file) "K")
      dest-file)))

(defn download-manga [name episode-nr]
  (let [url (compose-url name episode-nr)
        source (fetch-url url)
        page-urls (parse-page-urls source)
        image-to-name (pmap (comp download-image as-absolute) page-urls)
        dest-folder (format "%s-%s" name episode-nr)]
    (prepare-dest-folder dest-folder)
    [dest-folder (map #(store-img dest-folder %) image-to-name)]))

(defn print-banner [banner]
  (println "Absent required arguments. As a arguments specify manga name"
           "and episode number")
  (println "Example of arguments, lein run naruto 100")
  (println banner))

(defn -main [& arguments]
  (let [[options args banner] (c/cli arguments
            ["-v" "--verbose" "Be verbose" :flag true]
            ["-h" "--help" "Print this help message" :flag true ])
        query (join " " args)]
    (when (or (:help options)
              (not (seq args)))
      (print-banner banner)
      (System/exit 0))
    (when (:verbose options)
      (set-logger! :level :debug))
    (let [[name episode-nr] (parse-query query)
           [dest-folder img-paths] (download-manga name episode-nr)]
         (println (format "Downloaded %s images, saved to %s" (count img-paths) dest-folder)))))
