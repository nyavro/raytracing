(ns raytracing.render
  (:import java.awt.Color)
  (:require [raytracing.shapes :refer :all]))

(defn fillGrad [canvas]
  (let [w (.getWidth canvas)
        h (.getHeight canvas)
        full 255
        ]
    (defn calcColor [x y]
      (.getRGB (new Color (quot (* x full) w) (quot (* y full) h) 0 full))
    )
    (dorun
      (for [x (range 0 (.getWidth canvas)) y (range 0 (.getHeight canvas))] (.setRGB canvas x y (calcColor x y)))
      )
    )
)

(defn nullableToList [value]
  (if (nil? value) '() '(value))
)

(defn firstOrDefault [list default]
  (or (first list) default
  )
)

(defn min-by [fn col] (if (empty? col) nil (reduce #(if (< (fn %1) (fn %2)) %1 %2) col)))

(defn sceneIntersect [ray shapes]
  (min-by #(:distance %) (filter some? (map #(intersection % ray) shapes)))
)

(defn mulColor [color r]
  (defn md [c] (int (* c r)))
  (new Color (md (.getRed color)) (md (.getGreen color)) (md (.getBlue color)))
)

(defn renderDiffused [canvas shapes light distanceToCanvas defaultColor]
  (let [w (.getWidth canvas)
        h (.getHeight canvas)
        hw (/ w 2)
        hh (/ h 2)
        ]
    (defn calcColor [x y]
      (let [x' (- x hw) y' (- y hh)
            ray (raytracing.shapes.Ray. zero (normalize (struct Vector x' y' (- 0 distanceToCanvas))))
            intersection (sceneIntersect ray shapes)
            diffuseLightIntensity (
                                    if intersection
                                      (* (:intensity light) (max 0 (scalarMul (normalize (sub (:position light) (:hit intersection))) (:N intersection))))
                                      0.0
                                  )
           ]
          (if intersection
            (mulColor (diffuseColor (:material intersection)) diffuseLightIntensity)
            defaultColor
          )
      )
    )
    (dorun
        (for [x (range 0 w) y (range 0 h)] (.setRGB canvas x y (.getRGB (calcColor x y))))
    )
  )
)