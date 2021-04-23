package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERMODE")
public class UserMode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long uModeId;

    public void init(){
        throw new UnsupportedOperationException();
    };
}