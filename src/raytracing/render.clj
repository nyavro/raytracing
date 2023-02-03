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

(defn render [canvas sphere distanceToCanvas]
  (let [w (.getWidth canvas)
        h (.getHeight canvas)
        hw (/ w 2)
        hh (/ h 2)
        ]
    (defn calcColor [x y]
      (let [x' (- x hw) y' (- y hh)
            ray (raytracing.shapes.Ray. zero (normalize (struct Vector x' y' (- 0 distanceToCanvas))))]
        (.getRGB (if (intersects? sphere ray) Color/RED Color/GRAY))
      )
    )
    (dorun
      (for [x (range 0 (.getWidth canvas)) y (range 0 (.getHeight canvas))] (.setRGB canvas x y (calcColor x y)))
      )
    )
  )