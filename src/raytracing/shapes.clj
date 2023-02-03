(ns raytracing.shapes)

(defstruct Point :x :y :z)
(defstruct Vector :x :y :z)

(defn createVector [lst]
  "Take first 3 items of a list to a Vector"
  (let [[x y z] lst]
    (struct Vector x y z))
  )

(defn mul [v d]
  "Multiplication of vector to a number"
  (createVector (map #(* % d) (vals v)))
  )

(defn scalarMul [u v]
  "Scalar multiplication of two vectors"
  (reduce + (map #(* %1 %2) (vals u) (vals v))))

(defn vectorMul [u v]
  "Vector multiplication of two vectors"
  (struct Vector
          (- (*(u :y) (v :z)) (*(u :z) (v :y)))
          (- (*(u :z) (v :x)) (*(u :x) (v :z)))
          (- (*(u :x) (v :y)) (*(u :y) (v :x)))))

(defn add [& vs]
  "Adds vectors"
  (struct Vector
          (reduce + (map #(% :x) vs))
          (reduce + (map #(% :y) vs))
          (reduce + (map #(% :z) vs))))

(defn sub [u v]
  (struct Vector
          (- (u :x) (v :x))
          (- (u :y) (v :y))
          (- (u :z) (v :z))))

(def zero
  "Zero vector/point"
  (struct Point 0.0 0.0 0.0)
  )

(defn norm [u]
  "Vector norm (length)"
  (Math/sqrt (reduce + (map #(* % %) (vals u)))))

(defn normalize [v]
  (let [n (norm v)]
    (if(== 0 n) zero (mul v (/ 1.0 n)))
  )
)

(defn equals? [u v] (every? true? (map == (vals u) (vals v))))

(defn collinear? [u v]
  "Checks if two vectors are collinear"
  (equals? (vectorMul u v) zero))

;Materials
(defprotocol Material
  (diffuseColor [this] [this color])
)

(defrecord Rubber [color]
  Material
  (diffuseColor [_] color)
  (diffuseColor [this c] color)
)

(defrecord Ivory [color]
  Material
  (diffuseColor [_] color)
  (diffuseColor [_ _] color)
)

;Shapes
(defprotocol Shape
  (intersects? [this] [this ray])
  (distanceTo [this] [this point])
  (diffuse [this] [this color])
)

(defrecord Ray [start direction]
  Shape
  (intersects? [_ ray] false) ;//todo
  (distanceTo [_ point] (/ (norm (vectorMul (sub start point) direction)) (norm direction)))
  (diffuse [_ _] nil)
)

(defrecord Sphere [center r material]
  Shape
  (intersects? [_ ray]
    (and
      (< (distanceTo ray center) r)
      (> (scalarMul (:direction ray) (sub center (:start ray))) 0)
    )
  )
  (distanceTo [_ point] (- (norm (sub point center)) r))
  (diffuse [_ color] (diffuseColor material color))
  (diffuse [_] (diffuseColor material))
)

(defrecord DefaultShape [material]
  Shape
  (diffuse [_] (diffuseColor material))
)

(def ray (Ray. (struct Point 3 1 -1) (struct Vector 2 1 2)))
(def sphere (Sphere. (struct Point 0 2 3) 7 0))
(intersects? sphere ray)
