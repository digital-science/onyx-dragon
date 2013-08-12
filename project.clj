(defproject onyx-dragon "0.1.0-SNAPSHOT"
  :description "A basic script for checking a CloudFront distribution for invalidations forever"
  :url "https://github.com/digital-science/onyx-dragon"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [amazonica "0.1.13"]
                 [clj-campfire "2.2.0"]
                 [environ "0.4.0"]]
  :main onyx-dragon.core)
