import java.math.BigDecimal;
import java.util.*;

public class ConsistHash {


    private static class VisualNode {

        private String serverName;

        public VisualNode(String serverName) {
            this.serverName = serverName;
        }

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }
    }

    public static class ConsistHashCycle {

        private List<Integer> restSlotPosition = new ArrayList<>();

        private Integer step;

        private Integer totalHashSlot;

        private Integer numToEveryServer;

        private Map<Integer, VisualNode> visualNodeMap = new HashMap<>();

        public ConsistHashCycle(Integer totalHashSlot, Integer visualNodeNum, Integer step) {
            this.step = step;
            this.totalHashSlot = totalHashSlot;
            for ( int i = 0 ; i < this.totalHashSlot && i >= 0; ) {
                this.restSlotPosition.add(i);
                i += step;
            }
            this.numToEveryServer = this.restSlotPosition.size()/visualNodeNum;
        }

        public void addServer(String serverName) {
            if ( this.restSlotPosition.size() == 0 ) {
                throw new RuntimeException(" no any visualNode for the new server ");
            }
            Random random = new Random();
            for (int j = 0 ; j < this.numToEveryServer ; j++) {
                int pos = random.nextInt(this.restSlotPosition.size());
                this.visualNodeMap.put(this.restSlotPosition.get(pos), new VisualNode(serverName));
                this.restSlotPosition.remove(pos);
            }
        }

        public String getSeverName(Integer hashCode){
            if ( hashCode >= totalHashSlot ) {
                hashCode = hashCode % totalHashSlot;
            }
            int posIndex = (hashCode/this.step)*this.step;
            while( true ) {
                VisualNode node = this.visualNodeMap.get(posIndex);
                if ( node != null ) {
                    return node.serverName;
                } else {
                    posIndex += this.step;
                    if (posIndex >= this.totalHashSlot ) {
                        posIndex = 0;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Integer visualNodeNum = 10 ;
        Integer step = 32768 ;
        ConsistHashCycle cycle = new ConsistHashCycle(Integer.MAX_VALUE - 1, visualNodeNum, 32768);
        cycle.addServer("server1");
        cycle.addServer("server2");
        cycle.addServer("server3");
        cycle.addServer("server4");
        cycle.addServer("server5");
        cycle.addServer("server6");
        cycle.addServer("server7");
        cycle.addServer("server8");
        cycle.addServer("server9");
        cycle.addServer("server10");

        for ( int q= 0 ; q< 10 ; q++ ) {
            Map<String, Integer> stat = new HashMap<>();
            Random random = new Random();
            int totalTimes = 1000000;
            for ( int i = 0 ; i < totalTimes ; i++ ) {
                int hashCode = Math.abs(random.nextInt());
                String result = cycle.getSeverName(hashCode);
                stat.compute(result, (k,v)->{
                    if ( v == null ) {
                        return 1;
                    } else {
                        return v+1;
                    }
                });
            }

            Double avg  =  1.0d*totalTimes/visualNodeNum;
            Double total = 0d;
            for ( Integer value : stat.values()){
                total += Math.pow(value - avg,2);
            }

            System.out.println("标准差：" + Math.sqrt(total/visualNodeNum));
        }


    }






}
