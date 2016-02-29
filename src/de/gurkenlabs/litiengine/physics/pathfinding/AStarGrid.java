package de.gurkenlabs.litiengine.physics.pathfinding;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import de.gurkenlabs.litiengine.physics.IPhysicsEngine;
import de.gurkenlabs.tiled.tmx.IMap;

public class AStarGrid {
  private boolean allowDiagonalMovementOnCorners;
  private final Dimension size;
  private final int nodeSize;
  private final AStarNode[][] grid;

  public AStarGrid(final IPhysicsEngine physicsEngine, final IMap map, final int nodeSize) {
    this.size = map.getSizeInPixles();
    this.nodeSize = nodeSize;
    int gridSizeX = this.size.width / nodeSize;
    int gridSizeY = this.size.height / nodeSize;
    this.grid = new AStarNode[gridSizeX][gridSizeY];
    this.populateGrid(physicsEngine, gridSizeX, gridSizeY);
  }

  private void populateGrid(final IPhysicsEngine physicsEngine, final int gridSizeX, final int gridSizeY) {
    for (int x = 0; x < gridSizeX; x++) {
      for (int y = 0; y < gridSizeY; y++) {
        Rectangle nodeBounds = new Rectangle(x * nodeSize, y * nodeSize, nodeSize, nodeSize);

        // TODO: add terrain dependent penalty
        this.getGrid()[x][y] = new AStarNode(!physicsEngine.collides(nodeBounds), nodeBounds, x, y, 0);
      }
    }
  }

  public List<AStarNode> getNeighbours(AStarNode node) {

    List<AStarNode> neighbors = new ArrayList<AStarNode>();
    int x = node.getGridX();
    int y = node.getGridY();
    AStarNode top = this.getNode(x, y - 1);
    AStarNode bottom = this.getNode(x, y + 1);
    AStarNode left = this.getNode(x - 1, y);
    AStarNode right = this.getNode(x + 1, y);

    // diagonal
    AStarNode topLeft = this.getNode(x - 1, y - 1);
    AStarNode topRight = this.getNode(x + 1, y - 1);
    AStarNode bottomLeft = this.getNode(x - 1, y + 1);
    AStarNode bottomRight = this.getNode(x + 1, y + 1);

    if (top != null && top.isWalkable()) {
      neighbors.add(top);
    }

    if (bottom != null && bottom.isWalkable()) {
      neighbors.add(bottom);
    }

    if (right != null && right.isWalkable()) {
      neighbors.add(right);
    }

    if (left != null && left.isWalkable()) {
      neighbors.add(left);
    }

    // only add diogonal neighbours when they are not on a cornor
    if ((topLeft != null && this.diagonalMovementOnCorners()) || (topLeft != null && top.isWalkable() && left.isWalkable())) {
      neighbors.add(topLeft);
    }

    if ((topRight != null && this.diagonalMovementOnCorners()) || (topRight != null && top.isWalkable() && right.isWalkable())) {
      neighbors.add(topRight);
    }

    if ((bottomLeft != null && this.diagonalMovementOnCorners()) || (bottomLeft != null && bottom.isWalkable() && left.isWalkable())) {
      neighbors.add(bottomLeft);
    }

    if ((bottomRight != null && this.diagonalMovementOnCorners()) || (bottomRight != null && bottom.isWalkable() && right.isWalkable())) {
      neighbors.add(bottomRight);
    }

    return neighbors;
  }

  private AStarNode getNode(int x, int y) {
    if (x >= 0 && x < this.getGrid().length && y >= 0 && y < this.getGrid()[0].length) {
      return this.getGrid()[x][y];
    }

    return null;
  }

  public AStarNode getNodeFromMapLocation(Point2D point) {
    float percentX = (float) (point.getX() / this.getSize().getWidth());
    float percentY = (float) (point.getY() / this.getSize().getHeight());
    percentX = Math.max(0, Math.min(1, percentX));
    percentY = Math.max(0, Math.min(1, percentY));

    int x = (int) ((this.getGrid().length - 1) * percentX);
    int y = (int) ((this.getGrid()[0].length) * percentY);
    return this.getGrid()[x][y];
  }

  public int getNodeSize() {
    return this.nodeSize;
  }

  public AStarNode[][] getGrid() {
    return this.grid;
  }

  public Dimension getSize() {
    return this.size;
  }

  public boolean diagonalMovementOnCorners() {
    return this.allowDiagonalMovementOnCorners;
  }

  public void setAllowDiagonalMovementOnCorners(boolean allowDiagonalMovementOnCorners) {
    this.allowDiagonalMovementOnCorners = allowDiagonalMovementOnCorners;
  }
}