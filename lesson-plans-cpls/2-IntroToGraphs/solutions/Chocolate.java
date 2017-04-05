import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;

public class Chocolate {
    static class Step {
        public long minute;
        public long a, b;
        public Step(long minute, long a, long b)
        {
            this.minute = minute;
            this.a = a;
            this.b = b;
        }
        public String toString()
        {
            return ((Long) minute).toString() + " " + ((Long) a).toString() + " " + ((Long) b).toString();
        }
    }


    public static void main(String[] args) {

        Map<Long, Step> ASteps = new TreeMap<Long, Step>();
        Map<Long, Step> BSteps = new TreeMap<Long, Step>();

        Queue<Step> AQueue = new ArrayDeque<Step>();
        Queue<Step> BQueue = new ArrayDeque<Step>();

        Scanner s = new Scanner(System.in);
        long a1 = s.nextLong();
        long b1 = s.nextLong();
        long a2 = s.nextLong();
        long b2 = s.nextLong();

        AQueue.add(new Step(0, a1, b1));
        BQueue.add(new Step(0, a2, b2));

        boolean exited = false;

        while (!AQueue.isEmpty() || !BQueue.isEmpty()) {
            // meet in the middle bfs
            if (!AQueue.isEmpty()) {
                Step ACurrent = AQueue.poll();
                //System.out.println("A Curr: " + ACurrent);
                long ASize = ACurrent.a * ACurrent.b;
                if (!ASteps.containsKey(ASize)) {
                    if (BSteps.containsKey(ASize)) {
                        Step BPair = BSteps.get(ASize);
                        System.out.println(ACurrent.minute + BPair.minute);
                        System.out.println(ACurrent.a + " " + ACurrent.b);
                        System.out.println(BPair.a + " " + BPair.b);
                        exited = true;
                        break;
                    }

                    if (ACurrent.a % 2 == 0) {
                        AQueue.add(new Step(ACurrent.minute+1, ACurrent.a / 2, ACurrent.b));
                    }

                    if (ACurrent.a % 3 == 0) {
                        AQueue.add(new Step(ACurrent.minute+1, ACurrent.a * 2 / 3, ACurrent.b));
                    }

                    if (ACurrent.b % 2 == 0) {
                        AQueue.add(new Step(ACurrent.minute+1, ACurrent.a, ACurrent.b / 2));
                    }

                    if (ACurrent.b % 3 == 0) {
                        AQueue.add(new Step(ACurrent.minute+1, ACurrent.a, ACurrent.b * 2 / 3));
                    }
                    ASteps.put(ASize, ACurrent);
                }
            }

            if (!BQueue.isEmpty()) {
                Step BCurrent = BQueue.poll();
                //System.out.println("B Curr: " + BCurrent);
                long BSize = BCurrent.a * BCurrent.b;
                if (!BSteps.containsKey(BSize)) {
                    if (ASteps.containsKey(BSize)) {
                        Step APair = ASteps.get(BSize);
                        System.out.println(BCurrent.minute + APair.minute);
                        System.out.println(APair.a + " " + APair.b);
                        System.out.println(BCurrent.a + " " + BCurrent.b);
                        exited = true;
                        break;
                    }

                    if (BCurrent.a % 2 == 0) {
                        BQueue.add(new Step(BCurrent.minute+1, BCurrent.a / 2, BCurrent.b));
                    }

                    if (BCurrent.a % 3 == 0) {
                        BQueue.add(new Step(BCurrent.minute+1, BCurrent.a * 2 / 3, BCurrent.b));
                    }

                    if (BCurrent.b % 2 == 0) {
                        BQueue.add(new Step(BCurrent.minute+1, BCurrent.a, BCurrent.b / 2));
                    }

                    if (BCurrent.b % 3 == 0) {
                        BQueue.add(new Step(BCurrent.minute+1, BCurrent.a, BCurrent.b * 2 / 3));
                    }
                    BSteps.put(BSize, BCurrent);
                }
            }
        }

        if (!exited)
        {
            System.out.println("-1");
        }

    }



}
