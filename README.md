# manga-clj

A "script" developed to download manga comics and written in Clojure. Inspired by [mangapy](https://github.com/cybercam/mangapy/). The project is made just for fun but I will be interested in any feedback.

As a reader for the comics I use [SimpleComic](http://dancingtortoise.com/simplecomic/)

## Usage

Currently there is no some kind distribution package

```
As a arguments specify manga name and episode number
Usage: lein run <manga name> <episode number>

 Switches                     Default  Desc                    
 --------                     -------  ----                    
 -v, --no-verbose, --verbose  false    Be verbose              
 -h, --no-help, --help        false    Print this help message 
```

Example, 

`lein run naruto 100`

## Roadmap

- [ ] add optional possibility to zip images into archive
- [ ] make package for manga-clj distribution as CLI to decouple from Leiningen

## Used resources

* [How to download and store images in Java](http://www.developerfeed.com/imageio/topic/how-download-image-url-and-save-it-java)

## License

The MIT License (MIT)

Copyright (c) [2013] [Volodymyr Vitvitskyi]

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
