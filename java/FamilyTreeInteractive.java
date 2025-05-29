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
                    if (p1.isSiblingOf(p2)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    static Map<String, Person> people = new HashMap<>();

    public static Person getOrCreatePerson(String name, String gender) {
        return people.computeIfAbsent(name, n -> new Person(n, gender));
    }

    private static void setParentChild(String parentName, String childName) {
        Person parent = people.get(parentName);
        Person child = people.get(childName);
        parent.addChild(child);
        if (parent.spouse != null) {
            parent.spouse.addChild(child);
        }
    }

    public static void buildFamilyTree() {
        // Fact #1
        String[] males = {"Andy", "Bob", "Cecil", "Dennis", "Edward", "Felix", "Martin", "Oscar", "Quinn"};
        String[] females = {"Gigi", "Helen", "Iris", "Jane", "Kate", "Liz", "Nancy", "Pattie", "Rebecca"};

        for (String name : males) {
            getOrCreatePerson(name, "male");
        }
        for (String name : females) {
            getOrCreatePerson(name, "female");
        }

        // Fact #2
        getOrCreatePerson("Bob", "male").marry(getOrCreatePerson("Helen", "female"));
        getOrCreatePerson("Dennis", "male").marry(getOrCreatePerson("Pattie", "female"));
        getOrCreatePerson("Martin", "male").marry(getOrCreatePerson("Gigi", "female"));

        // Fact #3
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
        System.out.println("請輸入兩個名字來查詢關係（輸入 exit 離開）");

        while (true) {
            System.out.print("name1: ");
            String name1 = scanner.nextLine().trim();
            if (name1.equalsIgnoreCase("exit")) break;

            System.out.print("name2: ");
            String name2 = scanner.nextLine().trim();
            if (name2.equalsIgnoreCase("exit")) break;

            Person p1 = people.get(name1);
            Person p2 = people.get(name2);

            if (p1 == null || p2 == null) {
                System.out.println("找不到其中一位或兩位人物，請再試一次。");
                continue;
            }

            // 查詢關係
            if (p1.isBrotherOf(p2)) {
                System.out.println(name1 + " 和 " + name2 + " 是兄弟");
            } else if (p1.isSisterOf(p2)) {
                System.out.println(name1 + " 和 " + name2 + " 是姐妹");
            } else if (p1.isSiblingOf(p2)) {
                System.out.println(name1 + " 和 " + name2 + " 是兄弟姊妹");
            } else if (p1.isCousinOf(p2)) {
                System.out.println(name1 + " 和 " + name2 + " 是堂表兄妹");
            } else {
                System.out.println(name1 + " 和 " + name2 + " 沒有特殊的親屬關係。");
            }
        }

        scanner.close();
        System.out.println("查詢結束，謝謝使用！");
    }
}
