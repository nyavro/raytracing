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

(defn render [canvas shapes distanceToCanvas defaultColor]
  (let [w (.getWidth canvas)
        h (.getHeight canvas)
        hw (/ w 2)
        hh (/ h 2)
        ]
    (defn calcColor [x y]
      (let [x' (- x hw) y' (- y hh)
            ray (raytracing.shapes.Ray. zero (normalize (struct Vector x' y' (- 0 distanceToCanvas))))]
        (diffuse
          (or
            (some #(when (intersects? % ray) %) shapes)
            defaultColor
          )
        )
      )
    )
    (dorun
      (for [x (range 0 w) y (range 0 h)] (.setRGB canvas x y (calcColor x y)))
      )
    )
  )