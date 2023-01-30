(ns shapes)

(defstruct Point :x :y :z)
(defstruct Vector :x :y :z)

(defn fromList [lst]
  (let [[x y z] lst]
    (struct Vector x y z))
  )

(defn mul [v d]
  (fromList (map #(* % d) (vals v)))
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

(def zero (struct Point 0.0 0.0 0.0))

(defn norm [u]
  "Vector norm (length)"
  (Math/sqrt (reduce + (map #(* % %) (vals u)))))

(defn equals? [u v] (every? true? (map == (vals u) (vals v))))

(defn collinear? [u v]
  "Checks if two vectors are collinear"
  (equals? (vectorMul u v) zero))

(defprotocol Shape
  (intersects [ray])
  (distanceTo [point])
  )

