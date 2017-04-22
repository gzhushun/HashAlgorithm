package Blake;

import java.io.*;

public class BLAKE256 {

	private static BLAKEHash bh = new BLAKEHash();
	
	public static void main(String[] args) {
	    long messageTotalLength;
	    
	    //OutputStream os = new BufferedOutputStream(new FileOutputStream(args[1]));
		// cut the plain text to blocks of 64 bytes and give it to digest
	    try {
		    InputStream is = new BufferedInputStream (new FileInputStream(new File (args[0])));
		    messageTotalLength = is.available();
	    byte[] b = new byte[1];
	    while ( is.available() > 0){
	    	is.read(b, 0, 1);
	    	//System.out.print(new String(b) + " ");
	    	bh.hash((int)b[0]);
	    	
	    }
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    DataOutputStream dos = new DataOutputStream(baos);
	    dos.writeLong(messageTotalLength);
	    dos.close();
	    byte[] len = baos.toByteArray();
	    baos.close();
	    
	    byte currentpad = (byte)128;
	    
	    while (messageTotalLength % 64 != 55){
	    	bh.hash((int)currentpad);
	    	currentpad = 0; 
	    	messageTotalLength ++;
	    }
	    
	    bh.hash((currentpad ^ 1));
	    
	    for (int i = 0; i < 8; i++){
	    	bh.hash((int)len[i]);
	    }
	    byte[] BLAKEOutput = new byte[32];
	    bh.digest(BLAKEOutput);
	    
	    OutputStream os = new BufferedOutputStream (new FileOutputStream(new File (args[1])));
	    os.write(BLAKEOutput);
	    
	    
	    is.close();
	    os.close();
	    } catch (IOException e) {
	   	 System.err.println("ERROR! Input output stream mulfunction:");
	   	 e.printStackTrace();
	    }

	}
	
}
