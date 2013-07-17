(defproject manga-clj "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main manga-clj.core
  :dependencies [[org.clojure/clojure "1.5.1"]
                 ;; for HTML parsing
                 [enlive "1.1.1"]
                 ;; logging
                 [org.clojure/tools.logging "0.2.6"]
                 [clj-logging-config "1.9.10"]
                 ;; for command line options processing
                 [org.clojure/tools.cli "0.2.2"]])
