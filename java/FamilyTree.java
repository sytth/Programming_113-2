import java.util.*;

public class FamilyTree {

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

    public static void main(String[] args) {
        // Fact #1: Gender
        String[] males = {"Andy", "Bob", "Cecil", "Dennis", "Edward", "Felix", "Martin", "Oscar", "Quinn"};
        String[] females = {"Gigi", "Helen", "Iris", "Jane", "Kate", "Liz", "Nancy", "Pattie", "Rebecca"};

        for (String name : males) {
            getOrCreatePerson(name, "male");
        }
        for (String name : females) {
            getOrCreatePerson(name, "female");
        }

        // Fact #2: Marriages
        getOrCreatePerson("Bob", "male").marry(getOrCreatePerson("Helen", "female"));
        getOrCreatePerson("Dennis", "male").marry(getOrCreatePerson("Pattie", "female"));
        getOrCreatePerson("Martin", "male").marry(getOrCreatePerson("Gigi", "female"));

        // Fact #3: Parent-child relationships
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

        // test Liz 和 Rebecca 是不是堂表兄妹？
        Person liz = people.get("Liz");
        Person rebecca = people.get("Rebecca");

        if (liz.isCousinOf(rebecca)) {
            System.out.println("Liz and Rebecca are cousins.");
        } else {
            System.out.println("Liz and Rebecca are NOT cousins.");
        }
    }

    private static void setParentChild(String parentName, String childName) {
        Person parent = people.get(parentName);
        Person child = people.get(childName);
        parent.addChild(child);

        // Relation #1: 配偶也是父母
        if (parent.spouse != null) {
            parent.spouse.addChild(child);
        }
    }
}
