import de.pauhull.onedayvaro.util.UUIDFetcher;

import java.util.UUID;

public class Test {

    public static void main(String[] args) {

        UUIDFetcher uuidFetcher = new UUIDFetcher();
        long t1 = System.currentTimeMillis();
        System.out.println(uuidFetcher.getNameSync(UUID.fromString("8f498fa9-b7aa-4027-8a91-f111c5e7d41a")));
        System.out.println("Took " + (System.currentTimeMillis() - t1) + "ms");
    }

}
