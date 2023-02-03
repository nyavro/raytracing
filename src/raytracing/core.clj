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
      rubber (raytracing.shapes.Rubber. (.getRGB (new Color 0 0 0)))
      ivory (raytracing.shapes.Rubber. (.getRGB (new Color 100 100 0)))
      default (raytracing.shapes.DefaultShape. (raytracing.shapes.Rubber. (.getRGB (new Color 100 100 100))))
    ]
    do
    ;(fillGrad canvas)
      (render
        canvas
        [
          (raytracing.shapes.Sphere. (struct Point 300 300 -4000) 100 ivory)
          (raytracing.shapes.Sphere. (struct Point -300 -300 -4000) 150 rubber)
          (raytracing.shapes.Sphere. (struct Point 100 -100 -3000) 300 rubber)
        ]
        2000
        default
      )
      (runme canvas)
  )
)

