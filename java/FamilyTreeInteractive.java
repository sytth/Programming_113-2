import java.util.*;

public class FamilyTreeInteractive {

    static class Person {
        String name;
        String gender;
        Person spouse;
        Person parent1, parent2;
        List<Person> children = new ArrayList<>();

        Person(String name, String gender) {
            this.name = name;
            this.gender = gender;
        }

        void addChild(Person child) {
            children.add(child);
            if (child.parent1 == null) {
                child.parent1 = this;
            } else if (child.parent2 == null && child.parent1 != this) {
                child.parent2 = this;
            }
        }

        void marry(Person partner) {
            this.spouse = partner;
            partner.spouse = this;
        }
        // 查詢父母與兄弟姊妹
        List<Person> getParents() {
            List<Person> parents = new ArrayList<>();
            if (parent1 != null) parents.add(parent1);
            if (parent2 != null) parents.add(parent2);
            return parents;
        }

        List<Person> getSiblings() {
            Set<Person> siblings = new HashSet<>();
            for (Person parent : getParents()) {
                siblings.addAll(parent.children);
            }
            siblings.remove(this);
            return new ArrayList<>(siblings);
        }

        // 判斷關係
        boolean isSiblingOf(Person other) {
            return this.getSiblings().contains(other);
        }

        boolean isBrotherOf(Person other) {
            return this.gender.equals("male") && isSiblingOf(other) && other.gender.equals("male");
        }

        boolean isSisterOf(Person other) {
            return this.gender.equals("female") && isSiblingOf(other) && other.gender.equals("female");
        }

        boolean isCousinOf(Person other) {
            for (Person p1 : this.getParents()) {
                for (Person p2 : other.getParents()) {
                    if (p1 != null && p2 != null && p1.isSiblingOf(p2)) {
                        return true;
                    }
                }
            }
            return false;
        }

        boolean isSpouseOf(Person other) {
            return this.spouse == other;
        }

        boolean isParentOf(Person child) {
            return this.children.contains(child);
        }

        boolean isChildOf(Person parent) {
            return parent.children.contains(this);
        }
    }

    // Map to store all people by name
    static Map<String, Person> people = new HashMap<>();

    // 取得或創建一個指定名字與性別的人
    public static Person getOrCreatePerson(String name, String gender) {
        return people.computeIfAbsent(name, n -> new Person(n, gender));
    }

    // 設定父母與子女之間的關係
    private static void setParentChild(String parentName, String childName) {
        Person parent = people.get(parentName);
        Person child = people.get(childName);
        parent.addChild(child);
        if (parent.spouse != null) {
            parent.spouse.addChild(child);
        }
    }

    // 建立整個家譜的資料
    public static void buildFamilyTree() {
        String[] males = {"Andy", "Bob", "Cecil", "Dennis", "Edward", "Felix", "Martin", "Oscar", "Quinn"};
        String[] females = {"Gigi", "Helen", "Iris", "Jane", "Kate", "Liz", "Nancy", "Pattie", "Rebecca"};

        // Create male persons
        for (String name : males) getOrCreatePerson(name, "male");
        // Create female persons
        for (String name : females) getOrCreatePerson(name, "female");

        // Set marriages
        getOrCreatePerson("Bob", "male").marry(getOrCreatePerson("Helen", "female"));
        getOrCreatePerson("Dennis", "male").marry(getOrCreatePerson("Pattie", "female"));
        getOrCreatePerson("Martin", "male").marry(getOrCreatePerson("Gigi", "female"));

        // Set parent-child relationships (tree structure)
        setParentChild("Andy", "Bob");
        setParentChild("Bob", "Cecil");
        setParentChild("Cecil", "Dennis");
        setParentChild("Dennis", "Edward");
        setParentChild("Edward", "Felix");

        setParentChild("Gigi", "Helen");
        setParentChild("Helen", "Iris");
        setParentChild("Iris", "Jane");
        setParentChild("Jane", "Kate");
        setParentChild("Kate", "Liz");

        setParentChild("Martin", "Nancy");
        setParentChild("Nancy", "Oscar");
        setParentChild("Oscar", "Pattie");
        setParentChild("Pattie", "Quinn");
        setParentChild("Quinn", "Rebecca");
    }

    public static void main(String[] args) {
        buildFamilyTree();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter two names and a relationship type.");
        System.out.println("Valid relationships: spouse, parent, child, sibling, brother, sister, cousin.");
        System.out.println("Type 'exit' to quit.");

        while (true) {
            System.out.print("Enter name1: ");
            String name1 = scanner.nextLine().trim();
            if (name1.equalsIgnoreCase("exit")) break;

            System.out.print("Enter name2: ");
            String name2 = scanner.nextLine().trim();
            if (name2.equalsIgnoreCase("exit")) break;

            System.out.print("Enter relationship type: ");
            String relationship = scanner.nextLine().trim().toLowerCase();
            if (relationship.equalsIgnoreCase("exit")) break;

            Person p1 = people.get(name1);
            Person p2 = people.get(name2);

            if (p1 == null || p2 == null) {
                System.out.println("One or both persons not found.\n");
                continue;
            }

            boolean result = false;
            switch (relationship) {
                case "spouse":
                    result = p1.isSpouseOf(p2);
                    break;
                case "parent":
                    result = p1.isParentOf(p2);
                    break;
                case "child":
                    result = p1.isChildOf(p2);
                    break;
                case "sibling":
                    result = p1.isSiblingOf(p2);
                    break;
                case "brother":
                    result = p1.isBrotherOf(p2);
                    break;
                case "sister":
                    result = p1.isSisterOf(p2);
                    break;
                case "cousin":
                    result = p1.isCousinOf(p2);
                    break;
                default:
                    System.out.println("Unknown relationship type. Please use: spouse, parent, child, sibling, brother, sister, cousin.\n");
                    continue;
            }

            if (result) {
                System.out.printf("%s and %s are %s.\n\n", name1, name2, relationship);
            } else {
                System.out.printf("%s and %s are not %s.\n\n", name1, name2, relationship);
            }
        }

        scanner.close();
    }
}
