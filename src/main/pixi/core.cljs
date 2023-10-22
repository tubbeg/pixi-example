(ns pixi.core
  (:require 
   ["pixi.js" :refer [Application
                      Sprite
                      Assets
                      Texture
                      Container]] 
   [cljs.core.async :refer [go]] 
   [cljs.core.async.interop :refer-macros [<p!]]))


(def bdy (.-body js/document))

(defn createSprite [t]
  (new Sprite t))

(defn loadAsset [path]
  (. Assets (load path)))

(def p1 "https://pixijs.com/assets/bunny.png")
;(def t (. Texture from "./bunny3.png"))

(def a (new Application))
(def stage (.-stage a))

(defn addToTicker [f] (. (. a -ticker) add f))
(.appendChild (.-body js/document) (.-view a))


(def half 0.5)
(def w (.. a -renderer -width))
(def h (.. a -renderer -height))


(defn setBunnyProperties [bunny] 
  (set! (.-x bunny) (/ w 2))
  (set! (.-y bunny) (/ h 2))
  (set! (.. bunny -anchor -x) half)
  (set! (.. bunny -anchor -y) half))


(defn addRote [c]
  (fn [delta] 
    (let [oldRote (. c -rotation) 
          newRote (- oldRote (* delta (- 0 0.005)))] 
      (set! (. c -rotation) newRote))))

(defn startPixi []
  (go
    ;note that loading assets is asynchronous, which is
    ;why we use <p! and go
    (let [t (<p! (loadAsset p1))
          bunny (createSprite t)
          c (-> (new Container)
                (. addChild bunny))]
      (setBunnyProperties bunny)
      (. stage addChild c)
      (addToTicker (addRote c)))))


(defn initFn []
  (println "application started")
  (startPixi))