datatype gender = Male | Female;

val genders = [
    ("Andy", Male), ("Bob", Male), ("Cecil", Male), ("Dennis", Male),
    ("Edward", Male), ("Felix", Male), ("Martin", Male), ("Oscar", Male),
    ("Quinn", Male), ("Gigi", Female), ("Helen", Female), ("Iris", Female),
    ("Jane", Female), ("Kate", Female), ("Liz", Female), ("Nancy", Female),
    ("Pattie", Female), ("Rebecca", Female)
];

val marriages = [
    ("Bob", "Helen"), ("Helen", "Bob"),
    ("Dennis", "Pattie"), ("Pattie", "Dennis"),
    ("Gigi", "Martin"), ("Martin", "Gigi")
];

val parentChild = [
    ("Andy", "Bob"), ("Bob", "Cecil"), ("Cecil", "Dennis"),
    ("Dennis", "Edward"), ("Edward", "Felix"),
    ("Gigi", "Helen"), ("Helen", "Iris"), ("Iris", "Jane"),
    ("Jane", "Kate"), ("Kate", "Liz"),
    ("Martin", "Nancy"), ("Nancy", "Oscar"), ("Oscar", "Pattie"),
    ("Pattie", "Quinn"), ("Quinn", "Rebecca")
];

fun genderOf name = 
    case List.find (fn (n, _) => n = name) genders of
        SOME (_, g) => SOME g
      | NONE => NONE;

fun isMarried (x, y) = List.exists (fn (a, b) => a = x andalso b = y) marriages;

fun parentsOf child =
    let
        val directParents = List.filter (fn (p, c) => c = child) parentChild
        fun addSpouse (p, _) = 
            case List.find (fn (a, b) => a = p) marriages of
                SOME (_, spouse) => [p, spouse]
              | NONE => [p]
    in
        List.concat (List.map addSpouse directParents)
    end;

fun siblings name =
    let
        val ps = parentsOf name
        val sibs = List.concat (List.map (fn p => List.map #2 (List.filter (fn (par, _) => par = p) parentChild)) ps)
    in
        List.filter (fn n => n <> name) sibs
    end;

fun isSibling (x, y) = List.exists (fn sib => sib = y) (siblings x);

fun isBrother (x, y) =
    isSibling(x, y) andalso genderOf x = SOME Male andalso genderOf y = SOME Male;

fun isSister (x, y) =
    isSibling(x, y) andalso genderOf x = SOME Female andalso genderOf y = SOME Female;

fun cousins x =
    let
        val ps = parentsOf x
        val auntsUncles = List.concat (List.map siblings ps)
        val cousinsList = List.concat (List.map (fn p => List.map #2 (List.filter (fn (par, _) => par = p) parentChild)) auntsUncles)
    in
        List.filter (fn n => n <> x) cousinsList
    end;

fun isCousin (x, y) = List.exists (fn c => c = y) (cousins x);

(* check *)
fun checkRelation (x, y, relation) =
    case relation of
        "sibling" => if isSibling(x, y) then "Yes, they are siblings\n" else "No, they are not siblings\n"
      | "brother" => if isBrother(x, y) then "Yes, they are brothers\n" else "No, they are not brothers\n"
      | "sister" => if isSister(x, y) then "Yes, they are sisters\n" else "No, they are not sisters\n"
      | "cousin" => if isCousin(x, y) then "Yes, they are cousins\n" else "No, they are not cousins\n"
      | "parent" => if List.exists (fn p => p = x) (parentsOf y) then "Yes, " ^ x ^ " is a parent of " ^ y ^ "\n" else "No, " ^ x ^ " is not a parent of " ^ y ^ "\n"
      | "child" => if List.exists (fn p => p = y) (parentsOf x) then "Yes, " ^ x ^ " is a child of " ^ y ^ "\n" else "No, " ^ x ^ " is not a child of " ^ y ^ "\n"
      | "married" => if isMarried(x, y) then "Yes, " ^ x ^ " and " ^ y ^ " are married\n" else "No, " ^ x ^ " and " ^ y ^ " are not married\n"
      | _ => "Unknown relationship type: " ^ relation ^ "\n";

(* 測資 *)
val testCases = [
    ("Bob", "Helen", "married"),
    ("Cecil", "Dennis", "sibling"),
    ("Liz", "Rebecca", "sister"),
    ("Edward", "Quinn", "parent"),
    ("Felix", "Iris", "cousin"),
    ("Andy", "Bob", "parent"),
    ("Gigi", "Martin", "married"),
    ("Helen", "Gigi", "sibling"),
    ("Bob", "Andy", "child")
];

val _ = List.app (fn (a,b,rel) => print (checkRelation(a,b,rel))) testCases;
