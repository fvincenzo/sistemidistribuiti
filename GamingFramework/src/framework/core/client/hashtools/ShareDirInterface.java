/*
 * Created on Mar 18, 2005
 *
 */
package framework.core.client.hashtools;

/**
 * @author noname
 *
 */
public interface ShareDirInterface {
    public String getName();
	public long getSize();
	public void refresh();
	public boolean isIn(String name);
	public ShareDir[] getSubDir();
	public ShareFile[] getSubFile();
	public void TTHash();
}
