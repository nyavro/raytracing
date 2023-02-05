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
      canvas (new BufferedImage 800 600 BufferedImage/TYPE_INT_ARGB)
      rubber (raytracing.shapes.Rubber. (new Color 75 25 25))
      ivory (raytracing.shapes.Rubber. (new Color 100 100 75))
      wood (raytracing.shapes.Rubber. (new Color 200 100 75))
      defaultColor (new Color 100 100 100)
      default (raytracing.shapes.DefaultShape. (raytracing.shapes.Rubber. defaultColor))
      mainLight (struct Light (struct Point -2000 -2000 0) 1.0)
    ]
    do
    ;(fillGrad canvas)
      (renderDiffused
        canvas
        [
          (raytracing.shapes.Sphere. (struct Point 350 350 -4000) 200 ivory)
          (raytracing.shapes.Sphere. (struct Point -350 350 -4000) 100 wood)
          (raytracing.shapes.Sphere. (struct Point -300 -300 -4000) 150 rubber)
          (raytracing.shapes.Sphere. (struct Point 100 -100 -3000) 300 rubber)
          (raytracing.shapes.Sphere. (struct Point -200 -200 -3500) 200 ivory)
        ]
        mainLight
        2000
        defaultColor
      )
      (runme canvas)
  )
)

