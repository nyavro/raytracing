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
  (defn md [c] (min 255 (int (rem (* c r) 300))))
  (new Color (md (.getRed color)) (md (.getGreen color)) (md (.getBlue color)))
)

(defn addColors [a b]
  (defn md [x y] (min 255 (+ x y)))
  (new Color (md (.getRed a) (.getRed b)) (md (.getGreen a) (.getGreen b)) (md (.getBlue a) (.getBlue b)))
)

(defn renderDiffused [canvas shapes lights distanceToCanvas defaultColor]
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
                (reduce + (map #(* (:intensity %) (max 0 (scalarMul (normalize (sub (:position %) (:hit intersection))) (:N intersection)))) lights))
                0.0
            )
           ]
          (if intersection
            (addColors
              (mulColor (:color (:material intersection)) diffuseLightIntensity)
              (new Color 255 255 255)
            )
            defaultColor
          )
      )
    )
    (dorun
        (for [x (range 0 w) y (range 0 h)] (.setRGB canvas x y (.getRGB (calcColor x y))))
    )
  )
)

(defn reflect [v N] (sub v (mul N (* (scalarMul v N) 2.0))))

(defn renderSpecular [canvas shapes lights distanceToCanvas defaultColor]
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
                (reduce + (map #(* (:intensity %) (max 0.0 (scalarMul (normalize (sub (:position %) (:hit intersection))) (:N intersection)))) lights))
                0.0
              )
            specularLightIntensity (
              if intersection
                (reduce +
                  (map
                    #(*
                       (:intensity %)
                       (Math/pow
                         (max
                           0.0
                           (let [lightDir (normalize (sub (:position %) (:hit intersection)))]
                             (scalarMul (reflect lightDir (:N intersection)) (:direction ray))
                           )
                         )
                         (:specularExponent (:material intersection))
                       )
                     )
                    lights
                  )
                )
                0.0
              )
            ]
        (if intersection
          (addColors
            (mulColor (:color (:material intersection)) (* diffuseLightIntensity (nth (:albedo (:material intersection)) 0)))
            (mulColor (new Color 255 255 255) (* specularLightIntensity (nth (:albedo (:material intersection)) 1)))
          )
          defaultColor
        )
      )
    )
  (dorun
    (for [x (range 0 w) y (range 0 h)] (.setRGB canvas x y (.getRGB (calcColor x y))))
    )
  )
)