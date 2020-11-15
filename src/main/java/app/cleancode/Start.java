package app.cleancode;

import app.cleancode.server.Server;

public class Start {
public static void main(String[] args) {
	Server server = new Server();
	server.run();
}
}
