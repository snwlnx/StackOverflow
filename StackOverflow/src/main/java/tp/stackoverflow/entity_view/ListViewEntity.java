package tp.stackoverflow.entity_view;

/**
 * Created by korolkov on 12/15/13.
 */
public interface ListViewEntity {

    public String getBody();

    public String getDate();

    public String getTitle();

    public String getDisplayName();

    public int    getEntityId();

    public byte[] getImage();

}
