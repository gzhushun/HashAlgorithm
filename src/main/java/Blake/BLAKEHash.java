package Blake;

public class BLAKEHash implements HashFunction{
	private final int digestSize = 32; // in BYTES which is 8 WORDS
	private int [] digest = {
			1779033703, -1150833019, 1013904242, -1521486534,
			 1359893119, -1694144372, 528734635, 1541459225
			};
	private final int [] constants = { 
			 608135816, -2052912941, 320440878, 57701188,	
			 -1542899678, 698298832, 137296536, -330404727,	
			 1160258022, 953160567, -1101764913, 887688300, 
			 -1062458953, -914599715, 1065670069, -1253635817 
			 }; 
	private int i = 0;
	private int [] message = new int [16]; 
	private int sigma[][] = 
		{{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15},
		{14,10,4,8,9,15,13,6,1,12,0,2,11,7,5,3},
		{11,8,12,0,5,2,15,13,10,14,3,6,7,1,9,4},
		{7,9,3,1,13,12,11,14,2,6,5,10,4,0,15,8},
		{9,0,5,7,2,4,10,15,14,1,11,12,6,8,3,13},
		{2,12,6,10,0,11,8,3,4,13,7,5,15,14,1,9},
		{12,5,1,15,14,13,4,10,0,7,6,3,9,2,8,11},
		{13,11,7,14,12,1,3,9,5,0,15,4,8,6,2,10},
		{6,15,14,9,11,3,0,8,12,2,13,7,1,4,10,5},
		{10,2,8,4,7,6,1,5,15,11,9,14,3,12,13,0}};
	private static int currentwordlengthinbytes = 0;
	private static byte [] currentword = new byte[4];
	

		

	// Size of the hash coming out of it. 1 byte = 8 bits. 8 * 32 = 256 bits
	public int digestSize() {
		return digestSize;
	}

	
	public void hash(int b) {
		bytePack((byte) b);
		if ( currentwordlengthinbytes == 4 ){
			int word = packByteArray(currentword);
			currentwordlengthinbytes = 0;
			message [i] = word; 
			++i;
			i %= 16;
		}
		if ((i + currentwordlengthinbytes) ==0){
			
			rounds();
		}
	}
	
	private void bytePack(byte b) {
		//System.out.print((char)b);
		currentword[currentwordlengthinbytes] = b;
		currentwordlengthinbytes ++;
		
	}

	private static int packByteArray(byte[] wordasbytes ){	
		int word =  (0xFF & wordasbytes [0]) << 24 | (0xFF & wordasbytes [1]) << 16 |(0xFF & wordasbytes [2]) << 8 |(0xFF & wordasbytes [3]);
		return word;
	}


	public void digest(byte[] d) {
		for (int i = 0; i < 8; i++ ){
			byte[] word =  {(byte)(Integer.rotateRight(digest[i],24)), (byte)(Integer.rotateRight(digest[i],16)),(byte)(Integer.rotateRight(digest[i],8)),(byte)(digest[i])};
			d[(4*i)] = word [0];
			d[(4*i)+1] = word [1];
			d[(4*i)+2] = word [2];
			d[(4*i)+3] = word [3];
		}
			//System.out.println(new String(d));
		
	}
	
	private void rounds(){
		int [][] m = initialize();

		for(int h = 0; h < 14; ++h)
		{
			// Index computations
			
		
			int j = sigma[h%10][0];
			int k = sigma[h%10][1];
			//Cols
			m[0][0] = m[0][0] + m[1][0] + (message[j] ^ constants[k]);   // Step 1 (with input)
			m[3][0] = Integer.rotateRight((m[3][0] ^ m[0][0]), 16);
			m[2][0] = m[2][0] + m[3][0];      // Step 2 (no input)
			m[1][0] = Integer.rotateRight((m[1][0] ^ m[2][0]), 12);

			m[0][0] = m[0][0] + m[1][0] + (message[k] ^ constants[j]);   // Step 3 (with input)
			m[3][0] = Integer.rotateRight((m[3][0] ^ m[0][0]), 8);
			m[2][0] = m[2][0] + m[3][0];       // Step 4 (no input)
			m[1][0] = Integer.rotateRight((m[1][0] ^ m[2][0]), 7);
			
			// Index computations
			j = sigma[h%10][2];
			k = sigma[h%10][3];
			
			//
			m[0][1] = m[0][1] + m[1][1] + (message[j] ^ constants[k]);   // Step 1 (with input)
			m[3][1] = Integer.rotateRight((m[3][1] ^ m[0][1]), 16);
			m[2][1] = m[2][1] + m[3][1];      // Step 2 (no input)
			m[1][1] = Integer.rotateRight((m[1][1] ^ m[2][1]), 12);

			m[0][1] = m[0][1] + m[1][1] + (message[k] ^ constants[j]);   // Step 3 (with input)
			m[3][1] = Integer.rotateRight((m[3][1] ^ m[0][1]), 8);
			m[2][1] = m[2][1] + m[3][1];       // Step 4 (no input)
			m[1][1] = Integer.rotateRight((m[1][1] ^ m[2][1]), 7);
			
			j = sigma[h%10][4];
			k = sigma[h%10][5];
						
			//
			m[0][2] = m[0][2] + m[1][2] + (message[j] ^ constants[k]);   // Step 1 (with input)
			m[3][2] = Integer.rotateRight((m[3][2] ^ m[0][2]), 16);
			m[2][2] = m[2][2] + m[3][2];      // Step 2 (no input)
			m[1][2] = Integer.rotateRight((m[1][2] ^ m[2][2]), 12);

			m[0][2] = m[0][2] + m[1][2] + (message[k] ^ constants[j]);   // Step 3 (with input)
			m[3][2] = Integer.rotateRight((m[3][2] ^ m[0][2]), 8);
			m[2][2] = m[2][2] + m[3][2];       // Step 4 (no input)
			m[1][2] = Integer.rotateRight((m[1][2] ^ m[2][2]), 7);
			
			j = sigma[h%10][6];
			k = sigma[h%10][7];
			//
			m[0][3] = m[0][3] + m[1][3] + (message[j] ^ constants[k]);   // Step 1 (with input)
			m[3][3] = Integer.rotateRight((m[3][3] ^ m[0][3]), 16);
			m[2][3] = m[2][3] + m[3][3];      // Step 2 (no input)
			m[1][3] = Integer.rotateRight((m[1][3] ^ m[2][3]), 12);

			m[0][3] = m[0][3] + m[1][3] + (message[k] ^ constants[j]);   // Step 3 (with input)
			m[3][3] = Integer.rotateRight((m[3][3] ^ m[0][3]), 8);
			m[2][3] = m[2][3] + m[3][3];       // Step 4 (no input)
			m[1][3] = Integer.rotateRight((m[1][3] ^ m[2][3]), 7);


			//Diagonals
					
			//a = m[0][g], b = m[1][(g+1)%4], c = m[2][(g+2)%4], d = m[3][(g+3)%4]

			j = sigma[h%10][8];
			k = sigma[h%10][9];
			
			m[0][0] = m[0][0] + m[1][1] + (message[j] ^ constants[k]);   // Step 1 (with input)
			m[3][3] = (m[3][3] ^ m[0][0]) >>> 16;
			m[2][2] = m[2][2] + m[3][3];      // Step 2 (no input)
			m[1][1] = (m[1][1] ^ m[2][2]) >>> 12;

			m[0][0] = m[0][0] + m[1][1] + (message[k] ^ constants[j]);   // Step 3 (with input)
			m[3][3] = (m[3][3] ^ m[0][0]) >>> 8;
			m[2][2] = m[2][2] + m[3][3];       // Step 4 (no input)
			m[1][1] = (m[1][1] ^ m[2][2]) >>> 7;
	
			j = sigma[h%10][10];
			k = sigma[h%10][11];
			//
			m[0][1] = m[0][1] + m[1][2] + (message[j] ^ constants[k]);   // Step 1 (with input)
			m[3][0] = (m[3][0] ^ m[0][1]) >>> 16;
			m[2][3] = m[2][3] + m[3][0];      // Step 2 (no input)
			m[1][2] = (m[1][2] ^ m[2][3]) >>> 12;

			m[0][1] = m[0][1] + m[1][2] + (message[k] ^ constants[j]);   // Step 3 (with input)
			m[3][0] = (m[3][0] ^ m[0][1]) >>> 8;
			m[2][3] = m[2][3] + m[3][0];       // Step 4 (no input)
			m[1][2] = (m[1][2] ^ m[2][3]) >>> 7;
				
			j = sigma[h%10][12];
			k = sigma[h%10][13];
			//
			m[0][2] = m[0][2] + m[1][3] + (message[j] ^ constants[k]);   // Step 1 (with input)
			m[3][1] = (m[3][1] ^ m[0][2]) >>> 16;
			m[2][0] = m[2][0] + m[3][1];      // Step 2 (no input)
			m[1][3] = (m[1][3] ^ m[2][0]) >>> 12;

			m[0][2] = m[0][2] + m[1][3] + (message[k] ^ constants[j]);   // Step 3 (with input)
			m[3][1] = (m[3][1] ^ m[0][2]) >>> 8;
			m[2][0] = m[2][0] + m[3][1];       // Step 4 (no input)
			m[1][3] = (m[1][3] ^ m[2][0]) >>> 7;

			j = sigma[h%10][14];
			k = sigma[h%10][15];
			//
			m[0][3] = m[0][3] + m[1][0] + (message[j] ^ constants[k]);   // Step 1 (with input)
			m[3][2] = (m[3][2] ^ m[0][3]) >>> 16;
			m[2][1] = m[2][1] + m[3][2];      // Step 2 (no input)
			m[1][0] = (m[1][0] ^ m[2][1]) >>> 12;

			m[0][3] = m[0][3] + m[1][0] + (message[k] ^ constants[j]);   // Step 3 (with input)
			m[3][2] = (m[3][2] ^ m[0][3]) >>> 8;
			m[2][1] = m[2][1] + m[3][2];       // Step 4 (no input)
			m[1][0] = (m[1][0] ^ m[2][1]) >>> 7;
						
		}
		finalize (m);
	}
	
	private int[][] initialize(){
		int[][] x = {{digest[0], digest[1], digest[2], digest[3]}, 
					{digest[4], digest[5], digest[6], digest[7]},
					{constants[0], constants[1], constants[2], constants[3]},
					{constants[4], constants[5], constants[6], constants[7]}};
		return x;
	}
	
	private void finalize(int[][] roundValues) {
		for(int i = 0; i < 8; i++)
			digest[i] = digest[i] ^ roundValues[i/4][i%4] ^ roundValues[(i/4)+2][i%4];	
	}
	
}
	
