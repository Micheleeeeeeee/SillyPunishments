package me.sillysock.sillypunishments.sillyapi.abstraction;

public abstract class AbstractDatabase {

    abstract void openConnection();

    abstract void closeConnection();

    abstract void resetConnection();
}