(ns raytracing.core
  (:import javax.swing.JPanel
           javax.swing.JFrame
           javax.swing.SwingUtilities
           javax.swing.WindowConstants
           java.awt.Dimension
           java.awt.image.BufferedImage
           java.awt.Graphics
           java.awt.Graphics2D
           java.awt.Color))

(defn fillGradient [canvas]
  (def w (.getWidth canvas))
  (def h (.getHeight canvas))
  (def full 255)
  (def calcColor [x y]
    (.getRGB (new Color (quot (* x full) w) (quot (* y full) h) 0 full))
  )
  (defn fillLine [y]
    (dorun
     (map #(.setRGB canvas % y (calcColor % y)) (range 0 w))
     )
    )
  (dorun
    (map fillLine (range 0 (.getHeight canvas)))
  )
)

(defn runme [canvas]
  (let [
         frame (new JFrame)
         panel (proxy
                     [JPanel] []
                     (paintComponent [g] (.drawImage g canvas 0 0 nil)))
        ]
    (
      (do
        (.setPreferredSize panel (new Dimension (.getWidth canvas) (.getHeight canvas)))
        (.add (.getContentPane frame) panel)
        (.setDefaultCloseOperation frame (JFrame/EXIT_ON_CLOSE))
        (.pack frame)
        (.setVisible frame true))
        frame
      )
    )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [canvas (new BufferedImage 800 600 BufferedImage/TYPE_INT_ARGB)]
  (do
    (fillGradient canvas)
    (.invokeLater SwingUtilities (runme canvas))
    )
    )
)

(-main)

