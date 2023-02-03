(ns raytracing.core
  (:import javax.swing.JPanel
           javax.swing.JFrame
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
  (let [canvas (new BufferedImage 800 600 BufferedImage/TYPE_INT_ARGB)]
   do
    ;(fillGrad canvas)
    (render canvas (raytracing.shapes.Sphere. (struct Point 300 300 -4000) 100) 2000)
    (runme canvas)
  )
)

