package ch.uzh.ifi.hase.soprafs21.deserialitzers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Clouds;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.SinglePlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;

public class GameDeserializer extends StdDeserializer<GameEntity>{

    public GameDeserializer(){
        this(null);
    }

    public GameDeserializer(Class<?> vc) { 
            super(vc); 
        }

    @Override
    public GameEntity deserialize(JsonParser jp, DeserializationContext ctxt) 
    throws IOException{
        JsonNode node = jp.getCodec().readTree(jp);
        Long userId =  node.get("userId").asLong();
        String userModeName = node.get("userMode").asText();
        String gameModeName = node.get("gameMode").asText();
        boolean publicStatus = node.get("publicStatus").asBoolean();

        UserMode uMode;
        GameMode gMode; 

        if (userModeName.equals("Singleplayer")){
            uMode = new SinglePlayer();
        } else if(userModeName.equals("Multiplayer")){
            uMode = new MultiPlayer();
        } else {
            throw new NotFoundException("Can not read this kind of Usermode");
        }

        switch (gameModeName) {
            case "Pixelation":
                gMode = new Pixelation(); 
                break;

            case "Time":
                gMode = new Time(); 
                break;

            case "Clouds":
                gMode = new Clouds(); 
                break;
        
            default:
                gMode = new Time();
                break;
        }
        
        GameEntity game = new GameEntity();
        game.setCreatorUserId(userId);
        game.setUserMode(uMode);
        game.setGameMode(gMode);
        game.setPublicStatus(publicStatus);

        return game;
    }
} 
