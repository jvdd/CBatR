package prismWrapper;

import java.io.IOException;
import java.io.InputStream;

public class ProcessMemoryLog implements Runnable {
	String pid;

	public ProcessMemoryLog(String pid) {
		this.pid = pid;
	}

	private volatile long memory;
	private volatile double cpu;
	private volatile double power;
	private volatile int samples;

	public long getMemory() {
		return memory;
	}

	public int getSamples() {
		return samples;
	}

	public double getCPU() {
		return cpu/samples;
	}

	public double getPower() {
		return power/samples;
	}

	public void resetValues() {
		memory = 0;
		cpu = 0;
		power = 0;
		samples = 0; 
	}

	@Override
	public void run() {
		ProcessBuilder pb = new ProcessBuilder("top", "-pid", pid, "-stats", "PID,MEM");
		pb.redirectError();
		StringBuilder str = new StringBuilder();
		try {
			Process p = pb.start();
			InputStream is = p.getInputStream();
			int value = -1;
			char ch;
			while ((value = is.read()) != -1) {
				ch = (char) value;
				if (ch == '\n') {
					parseLine(str.toString());
					str.setLength(0);
				} else {
					str.append(ch);
				}
				// System.out.print(((char)value));
			}
			int exitCode = p.waitFor();
			System.err.println("Top exited with " + exitCode);
		} catch (IOException exp) {
			exp.printStackTrace();
		} catch (InterruptedException ex) {
			// Logger.getLogger(JavaApplication256.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

	void parseLine(String line) {
		if (line.startsWith(pid)) {
			//System.err.println(line);
			samples++;
			String[] values = new String[3];
			String[] tokens = line.split(" ");
			int i = 0;
			for (String token : tokens) {
				if (!token.isEmpty()) {
					values[i++] = token;
				}
			}

			parseMemory(values[1]);
			//parseCPU(values[2]);
			//parsePower(values[3]);
		}
	}

	void parseMemory(String token) {
		if (token.endsWith("+") || token.endsWith("-")) {
			token = token.substring(0, token.length() - 1);
		}
		if (token.endsWith("M")) {
			long memory = Long.parseLong(token.substring(0, token.length() - 1));
			if (memory > this.memory) {
				this.memory = memory;
			}
		}
	}

	void parseCPU(String token) {
		double val = Double.parseDouble(token);
		cpu += val;
	}

	void parsePower(String token) {
		double val = Double.parseDouble(token);
		power += val;
	}
};