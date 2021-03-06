package roadsystem;
 import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 public class Trie {
	TrieNode root;
 	public Trie(){
		root = new TrieNode();
	}
 	public void add(Road r){
		char[] value = r.getRoadName().toCharArray();
		TrieNode node = root;
		for(Character c : value){
			//the char doesn't exist
			if(!node.children.containsKey(c)){
				TrieNode next = new TrieNode();
				node.children.put(c, next);
				node = next;
			}else{
				node = node.children.get(c);
			}
		}
		node.roads.add(r);
	}
 	public HashSet<Road> getAll(String s){
		HashSet<Road> results = new HashSet<Road>();
		char[] value = s.toCharArray();
		TrieNode node = root;
		for(Character c : value){
			if(!node.children.containsKey(c))
				return null;
			node = node.children.get(c);
		}
 		getAllFrom(node, results, s);
		return results;
	}
 	public void getAllFrom(TrieNode node, HashSet<Road> results, String s){
 		results.addAll(node.roads);
		for(Road r : node.roads){
			if(r.getRoadName().equals(s)){
				results = new HashSet<Road>();
				results.add(r);
				return;
			}
		}
		for(TrieNode n : node.children.values()){
			getAllFrom(n, results, s);
		}
		
	}
 	public String toString(){
		return root.toString();
	}
 	private class TrieNode{
		Set<Road> roads = null;
		HashMap<Character, TrieNode> children;
		private TrieNode(){
			children = new HashMap<Character, TrieNode>();
			roads = new HashSet<Road>();
		}
 		public String toString(){
			return "Children : " + children.keySet().toString() + " Roads " + roads.toString();
		}
 	}
}