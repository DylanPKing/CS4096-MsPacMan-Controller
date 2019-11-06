package pacman.controllers.examples;

import java.awt.*;
import java.util.Random;


import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.GHOST;
import pacman.controllers.Controller;
import pacman.game.GameView;



public class AttemptTwo extends Controller<MOVE> {

    private static final int MIN_DISTANCE=20;	//if a ghost is this close, run away

    public MOVE getMove(Game game, long timeDue) {

        int current=game.getPacmanCurrentNodeIndex();
        //get all active pills
        int[] activePills=game.getActivePillsIndices();

        //get all active power pills
        int[] activePowerPills=game.getActivePowerPillsIndices();


        /*----------------------------------Louise code-------------------------------------*/
        //Strategy 1: if any non-edible ghost is too close (less than MIN_DISTANCE), run away towards a power pill
        int numOfGhostsClose = 0;
        int closestGhostDistance = Integer.MAX_VALUE;
        GHOST closestGhost = GHOST.BLINKY;
        for(GHOST ghost : GHOST.values()) {
            // My addition - Szymon
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0)
                if (game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost)) < MIN_DISTANCE) {
                    for (int i = 0; i < activePowerPills.length; i++) {
                        if (((game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), activePowerPills[i])) < (game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), activePowerPills[i])))){
                            return  game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), activePowerPills[i], Constants.DM.PATH);
                        }
                        else{
                            for(int j = 0; j < activePills.length; j++){
                                if (((game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), activePills[i])) < (game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), activePowerPills[i])))) {
                                    return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), activePills[i], Constants.DM.PATH);
                                }
                            }
                        }

                    //return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.PATH);
                    numOfGhostsClose++;
                }
            if(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)) < closestGhostDistance) {
                closestGhostDistance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost));
                closestGhost = ghost;
            }
        }
        if(numOfGhostsClose >= 1) {
            /*for(GHOST ghost : GHOST.values()) {
                for (int i = 0; i < activePowerPills.length; i++) {
                    if (((game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), activePowerPills[i])) < (game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), activePowerPills[i])))){
                        return  game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), activePowerPills[i], Constants.DM.PATH);
                    }
                }*/
                return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(closestGhost), game.getPacmanLastMoveMade(), Constants.DM.PATH);
            }
        }

        /*----------------------------------End Louise code-------------------------------------*/
        //Strategy 2: find the nearest edible ghost and go after them
        int minDistance=Integer.MAX_VALUE;

        GHOST minGhost=null;


        for(GHOST ghost : GHOST.values()) {
            if (game.getGhostEdibleTime(ghost) > 0) {
                int distance = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));
                //System.out.println(distance);
                if (distance < minDistance) {
                    minDistance = distance;
                    minGhost = ghost;
                }
            }
        }
            if (minGhost != null )    //we found an edible ghost
                return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(minGhost), Constants.DM.PATH);



        //Strategy 3 go for pills which is taken from NearestPillPacMn


        //NearestPillPacMan
        int currentNodeIndex=game.getPacmanCurrentNodeIndex();

        //Let's try to create an array of just pills... Oh wait I already have one
        //Should make the power pills appear as walls? Let's try
        //create a target array that includes all ACTIVE pills and power pills
        int[] targetNodeIndices=new int[activePills.length+activePowerPills.length];

        for(int i=0;i<activePills.length;i++)
            targetNodeIndices[i]=activePills[i];

       /* for(int i=0;i<activePowerPills.length;i++)
            targetNodeIndices[activePills.length+i]=activePowerPills[i];*/


        //return the next direction once the closest target has been identified
        return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getClosestNodeIndexFromNodeIndex(currentNodeIndex,targetNodeIndices, Constants.DM.PATH), Constants.DM.PATH);
    }
}
