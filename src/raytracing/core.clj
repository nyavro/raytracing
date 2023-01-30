(ns raytracing.core
  (:import javax.swing.JPanel
           javax.swing.JFrame
           javax.swing.SwingUtilities
           javax.swing.WindowConstants
           java.awt.Dimension
           java.awt.image.BufferedImage
           java.awt.Graphics
           java.awt.Color))

(def canvas (new BufferedImage 100 100 BufferedImage/TYPE_INT_ARGB))
(.toString canvas)

(defn runme []
  (def width 800)
  (def height 600)
  (def frame (new JFrame))
  (def canvas (do (new BufferedImage width height BufferedImage/TYPE_INT_ARGB)))
  (def panel
    (proxy
     [JPanel] []
     (paintComponent [g] (.drawImage canvas nil nil));;(do (.setColor g Color/RED) (. g 0 0 (.getWidth this) (.getHeight this))))
     ))
  (do
    (.setPreferredSize panel (new Dimension width height))
    (.add (.getContentPane frame) panel)
    (.setDefaultCloseOperation frame (JFrame/EXIT_ON_CLOSE))
    (.pack frame)
    (.setVisible frame true))
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ())

(.invokeLater SwingUtilities (runme))
