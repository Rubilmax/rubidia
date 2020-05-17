package me.pmilon.RubidiaCore.utils;

import java.util.Collection;
import java.util.HashMap;

public abstract class Database<T,E> {

	private final HashMap<T,E> database = new HashMap<T,E>();
	
	public Collection<E> data(){
		return database.values();
	}
	
	public void load(T t,E e){
		this.database.put(t, e);
	}
	
	public void saveAll(boolean debug){
		this.onSaveStart(debug);
		for(E e : this.data()){
			this.save(debug,e);
		}
		this.onSaveEnd(debug);
	}
	
	public E get(T t){
		if(this.containsKey(t)){
			return this.database.get(t);
		}
		return null;
	}
	
	public E addDefault(T t){
		if(!this.containsKey(t)){
			E e = this.getDefault(t);
			this.load(t, e);
			return e;
		}
		return null;
	}
	
	public void remove(T t){
		this.database.remove(t);
	}
	
	public boolean containsKey(T t){
		return this.database.containsKey(t);
	}
	
	public boolean containsValue(E e){
		return this.database.containsValue(e);
	}
	
	public int size(){
		return this.database.size();
	}
	
	protected abstract void save(boolean debug, E e);

	protected abstract void onSaveStart(boolean debug);
	protected abstract void onSaveEnd(boolean debug);
	
	protected abstract E getDefault(T t);
	
}
