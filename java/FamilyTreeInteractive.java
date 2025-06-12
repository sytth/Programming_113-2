import java.util.*;

public class FamilyTreeInteractive {

    static class Person {
        String name;           // Person's name
        String gender;         // "male" or "female"
        Person spouse;         // Spouse
        Person parent1, parent2; // Two parents
        List<Person> children = new ArrayList<>(); // List of children

        // Constructor
        Person(String name, String gender) {
            this.name = name;
            this.gender = gender;
        }

        // Add child to this person
        void addChild(Person child) {
            children.add(child);
            // Assign this person as parent1 or parent2 of the child
            if (child.parent1 == null) {
                child.parent1 = this;
            } else if (child.parent2 == null && child.parent1 != this) {
                child.parent2 = this;
            }
        }

        // Marry another person (set each other as spouses)
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
            parent.spouse.addChild(child); // Both spouses are parents
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
        System.out.println("Please enter two names!\n Type 'exit' to quit.");

        while (true) {
            // Input name1
            System.out.print("Enter name1: ");
            String name1 = scanner.nextLine().trim();
            if (name1.equalsIgnoreCase("exit")) break;

            // Input name2
            System.out.print("Enter name2: ");
            String name2 = scanner.nextLine().trim();
            if (name2.equalsIgnoreCase("exit")) break;

            Person p1 = people.get(name1);
            Person p2 = people.get(name2);

            // Check if people exist
            if (p1 == null || p2 == null) {
                System.out.println("One or both persons not found.\n");
                continue;
            }

            // Relationship queries
            if (p1.isSpouseOf(p2)) {
                System.out.println(name1 + " and " + name2 + " are spouses.\n");
            } else if (p1.isParentOf(p2)) {
                System.out.println(name1 + " is a parent of " + name2 + ".\n");
            } else if (p1.isChildOf(p2)) {
                System.out.println(name1 + " is a child of " + name2 + ".\n");
            } else if (p1.isBrotherOf(p2)) {
                System.out.println(name1 + " and " + name2 + " are brothers.\n");
            } else if (p1.isSisterOf(p2)) {
                System.out.println(name1 + " and " + name2 + " are sisters.\n");
            } else if (p1.isSiblingOf(p2)) {
                System.out.println(name1 + " and " + name2 + " are siblings.\n");
            } else if (p1.isCousinOf(p2)) {
                System.out.println(name1 + " and " + name2 + " are cousins.\n");
            } else {
                System.out.println(name1 + " and " + name2 + " have no relationship.\n");
            }
        }

        scanner.close();
    }
}
