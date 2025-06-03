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

fun relationship (x, y) =
    if isSibling(x, y) then
        case (genderOf x, genderOf y) of
            (SOME Male, SOME Male) => x ^ " and " ^ y ^ " are brothers"
          | (SOME Female, SOME Female) => x ^ " and " ^ y ^ " are sisters"
          | _ => x ^ " and " ^ y ^ " are siblings"
    else if isCousin(x, y) then
        x ^ " and " ^ y ^ " are cousins"
    else if List.exists (fn p => p = x) (parentsOf y) then
        x ^ " is a parent of " ^ y
    else if List.exists (fn p => p = y) (parentsOf x) then
        y ^ " is a parent of " ^ x
    else if isMarried(x, y) then
        x ^ " and " ^ y ^ " are married"
    else
        "No direct relation found between " ^ x ^ " and " ^ y;

val testPairs = [
    ("Bob", "Helen"),
    ("Cecil", "Dennis"),
    ("Liz", "Rebecca"),
    ("Edward", "Quinn"),
    ("Felix", "Iris"),
    ("Andy", "Bob"),
    ("Gigi", "Martin")
];

val _ = List.app (fn (a,b) => print (relationship(a,b) ^ "\n")) testPairs;