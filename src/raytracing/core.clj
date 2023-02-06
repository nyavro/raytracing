(ns raytracing.core
  (:import javax.swing.JPanel
           javax.swing.JFrame
           java.awt.Color
           java.awt.Dimension
           java.awt.image.BufferedImage
  )
  (:require [raytracing.render :refer :all])
  (:require [raytracing.shapes :refer :all])
)

(defn runme [canvas]
  (let [
     frame (new JFrame)
     panel (proxy
       [JPanel] []
       (paintComponent [g] (.drawImage g canvas 0 0 nil))
     )
   ]
    (do
      (.setPreferredSize panel (new Dimension (.getWidth canvas) (.getHeight canvas)))
      (.add (.getContentPane frame) panel)
      (.setDefaultCloseOperation frame (JFrame/EXIT_ON_CLOSE))
      (.pack frame)
      (.setVisible frame true)
      frame
    )
  )
)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let
    [
      canvas (new BufferedImage 1024 768 BufferedImage/TYPE_INT_ARGB)
      rubber (struct Material (new Color 75 25 25) '(0.9 0.1) 10.0)
      ivory (struct Material (new Color 100 100 75) '(0.6 0.3) 50.0)
      wood (struct Material (new Color 200 100 75) '(0.6 0.3) 50.0)
      defaultColor (new Color 100 100 100)
    ]
    do
      (renderSpecular
        canvas
        [
          (raytracing.shapes.Sphere. (struct Point 350 350 -4000) 200 ivory)
          (raytracing.shapes.Sphere. (struct Point -350 350 -4000) 100 wood)
          (raytracing.shapes.Sphere. (struct Point -300 -200 -2000) 150 rubber)
          (raytracing.shapes.Sphere. (struct Point 100 -100 -3000) 300 rubber)
          (raytracing.shapes.Sphere. (struct Point -200 -200 -3500) 200 ivory)
        ]
        [
          (struct Light (struct Point -2000 -2000 2000) 0.7)
          (struct Light (struct Point 3000 -5000 -2500) 0.8)
          (struct Light (struct Point 3000 -2000 3000) 0.8)
        ]
        2000
        defaultColor
      )
      (runme canvas)
  )
)