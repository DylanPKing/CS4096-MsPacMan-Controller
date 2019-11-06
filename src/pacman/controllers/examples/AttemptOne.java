package pacman.controllers.examples;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;
import pacman.controllers.Controller;
import pacman.game.GameView;

public class AttemptOne extends Controller<MOVE> {

    private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away

    public MOVE getMove(Game game, long timeDue) {

        int current=game.getPacmanCurrentNodeIndex();
        //get all active pills
        int[] activePills=game.getActivePillsIndices();

        //get all active power pills
        int[] activePowerPills=game.getActivePowerPillsIndices();
/*        int closestPowerPill = activePowerPills[0];
        for(int i = 1; i < activePowerPills.length; i++) {
            if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), activePowerPills[i]) < game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),closestPowerPill)) {
                closestPowerPill = activePowerPills[i];
            }
        }

        if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), closestPowerPill) == 5) {

        }*/

        //Strategy 1: if any non-edible ghost is too close (less than MIN_DISTANCE), run away
        int numOfGhostsClose = 0;
        int closestGhostDistance = Integer.MAX_VALUE;
        GHOST closestGhost = GHOST.BLINKY;
        ArrayList<GHOST> nearGhosts = new ArrayList<GHOST>();
        for(GHOST ghost : GHOST.values()) {
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0)
                if (game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost)) < MIN_DISTANCE) {
                    //return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.PATH);
                    numOfGhostsClose++;

                }
            if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)) < closestGhostDistance) {
                closestGhostDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost));
                closestGhost = ghost;
            }

        }
        if(numOfGhostsClose >= 1) {
            return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), Constants.DM.PATH);
        }


        //Strategy 2: find the nearest edible ghost and go after them
        int minDistance=Integer.MAX_VALUE;
        GHOST minGhost=null;

        for(GHOST ghost : GHOST.values())
            if(game.getGhostEdibleTime(ghost)>0)
            {
                int distance=game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));

                if(distance<minDistance)
                {
                    minDistance=distance;
                    minGhost=ghost;
                }
            }

        if(minGhost!=null)	//we found an edible ghost
            return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(minGhost), Constants.DM.PATH);


        //Strategy 3 go for pills which is taken from NearestPillPacMn


        //NearestPillPacMan
        int currentNodeIndex=game.getPacmanCurrentNodeIndex();


        //create a target array that includes all ACTIVE pills and power pills
        int[] targetNodeIndices=new int[activePills.length+activePowerPills.length];

        for(int i=0;i<activePills.length;i++)
            targetNodeIndices[i]=activePills[i];

        for(int i=0;i<activePowerPills.length;i++)
            targetNodeIndices[activePills.length+i]=activePowerPills[i];


        //return the next direction once the closest target has been identified
        return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getClosestNodeIndexFromNodeIndex(currentNodeIndex,targetNodeIndices, Constants.DM.PATH), Constants.DM.PATH);
    }
}
