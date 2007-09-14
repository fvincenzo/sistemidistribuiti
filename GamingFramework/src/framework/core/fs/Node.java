package framework.core.fs;
import java.util.*;

/**
 * 
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class Node {
	private Dictionary childrenTable;
	private Node parentNode; 
	private String name;

	public static String nodeNameSeparator() {
		return "/"; 
	}

	public static String currentNodeIdentifier() {
		return ".";
	}

	public static String parentNodeIdentifier() {
		return "..";
	}

	/// <summary>
	/// Class constructor.
	/// </summary>
	public Node() 
	{
		childrenTable = null;
		parentNode = null;
		// TODO: Change unknown node name behaviour.
		setName("[unknown]"); 
	}

	/// <summary>
	/// Class constructor.
	/// </summary>
	public Node(String name) {
		childrenTable = null;
		parentNode = null;
		setName(name);
	}

	public Node addChild() {
		return addChild(new Node());
	}
	
	public Node addChild(Node n) {
		return addChild(n, n.getName());
	}

	public Node addChild(Node n, String name) throws NullPointerException {
		if(name != null) {
			if(n != null) {
				// Create children list instance, if none has been
				// already set for the node.
				if(childrenTable == null) {
					childrenTable = new Hashtable();
				}
				// Remove node from previous parent.
				n.remove();
				n.setName(name);
				n.parentNode = this;
				childrenTable.put(name, n);
			} else throw new NullPointerException();   
		} else throw new NullPointerException(); 
		return n;
	}
	/*public Node AddChild(Type t) {
	}
	public Node AddChild(Type t, string name) {
	}
	public Node AddChild(Type t, string name, Cluster c) {
	}*/
	public void remove() {
		if(parentNode != null) {
			parentNode.childrenTable.remove(name);
			parentNode = null;
		}
	}
	public Node findChild(String name) throws NullPointerException {
		Node res = null;
		if(name != null) {
			if(childrenTable != null) {
				res = (Node)childrenTable.get(name);
			}
		} else throw new NullPointerException(); 
		return res;
	}
	public Node find(String path) throws NullPointerException {
		Node curNode = null;
		if(path != null) {
			if(path.length() != 0) {
				if(path.startsWith(Node.nodeNameSeparator())) {
					curNode = this.root();
					path = path.substring(1);
				} else {
					curNode = this;
				}
				String[] pathElements = path.split(Node.nodeNameSeparator());
				int i;
				for(i = 0; i < pathElements.length; i++) {
					if(pathElements[i].equals(Node.parentNodeIdentifier())) {
						curNode = curNode.parent();
					} else if(!pathElements[i].equals(Node.currentNodeIdentifier())) {
						curNode = curNode.findChild(pathElements[i]); 
					} 
					if(curNode == null) break;
				}
			}
		} else throw new NullPointerException();
		return curNode;
	}
	/// <summary>
	///	Sets or retrieves the node name.
	/// </summary>
	/// <exception cref="ArgumentNullExeption">
	///	Occurs when trying to assing a null reference as node name.
	/// </exception>
	public String getName() {
		return name;
	}
	public void setName(String value) throws NullPointerException {
		if(value != null) {
			if(parentNode != null) {
				Node p = parentNode;
				p.childrenTable.remove(name);
				name = value;
				p.childrenTable.put(name, this);
			} else name = value;
		} else throw new NullPointerException(); 
	}
	/// <summary>
	///	Retrieves the node fullname.
	/// </summary>
	/// <remarks>
	/// [Describe fullname format]
	/// </remarks>
	public String fullname() {
		if(parent() != null) {
			return parent().fullname() + Node.nodeNameSeparator() + this.getName();
		} 
		return name;
	}
	/// <summary>
	///	Retrieves an istance of the node parent.
	/// </summary>
	public Node parent() {
		return parentNode;
	}
	/// <summary>
	/// Retrieves the root node of the hierarchy in which the
	/// node resides.
	/// </summary>
	public Node root() {
		if(parent() == null) return this;
		return parent().root();
	}
	/// <summary>
	///	Retrieves a collection of children instances for the node.
	/// </summary>
	public Enumeration children() {
		if(childrenTable == null) childrenTable = new Hashtable();
		return childrenTable.elements();
	}
	
	public String toString() {
		return fullname();
	}
	
	public String Dispatch(ParsedCmd cmd) {
		String res = null;
		if(cmd.Name.equals(".name")) {
			if(cmd.Params != null) {
				setName(cmd.Params[0]);
				res = "ok";
			} else res = getName();
		}
		return res;
	}
	
	public String getInfo() {
		
		String Info = "Generic Node";
		return Info;
		
	}

}
