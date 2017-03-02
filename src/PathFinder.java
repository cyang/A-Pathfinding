import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class PathFinder {
	private static int xLength = 6;
	private static int yLength = 7;
	private static boolean[][] coordinateMatrix = new boolean[xLength][yLength];
	private static List<Node> path;

	public static void main(String[] args) {
		for (int i = 0; i < xLength; i++) {
			for (int j = 0; j < yLength; j++) {
				coordinateMatrix[i][j] = true;
			}
		}

		coordinateMatrix[3][2] = false;
		coordinateMatrix[3][4] = false;
		coordinateMatrix[3][5] = false;
		coordinateMatrix[1][4] = false;

		Node start = new Node(1, 2);
		Node goal = new Node(4, 5);
		if (pathfind(start, goal)) {
			for (Node n : path) {
				System.out.println(n.getX() + ", " + n.getY());
			}
		}
	}

	private static boolean pathfind(Node start, Node goal) {
		List<Node> closedList = new ArrayList<Node>();
		Queue<Node> openList = new PriorityQueue<Node>(new ComparatorfScore());

		start.setgScore(0);
		start.setfScore(distanceBetween(start, goal));
		openList.add(start);

		while (!openList.isEmpty()) {
			// Get the node that has the lowest fScore
			Node current = openList.remove();

			if (current.equals(goal)) {
				path = reconstructPath(current);
				return true;
			}

			closedList.add(current);
			System.out.println(closedList.size());

			List<Node> neighbors = getNeighbors(current);
			for (Node n : neighbors) {
				if (closedList.contains(n)) {
					continue;
				}

				int tentativegScore = current.getgScore() + distanceBetween(current, n);
				if (!openList.contains(n)) {
					openList.add(n);
				} else if (tentativegScore >= n.getgScore()) {
					// Already in openList but the gScore is higher than
					// originally
					continue;
				}

				n.setPrev(current);
				n.setgScore(tentativegScore);
				n.setfScore(n.getgScore() + distanceBetween(n, goal));
			}
		}

		return false;
	}

	private static int distanceBetween(Node current, Node n) {
		return Math.abs(current.getX() - n.getX()) + Math.abs(current.getY() - n.getY());
	}

	private static List<Node> reconstructPath(Node goal) {
		Node cursor = goal;
		List<Node> totalPath = new ArrayList<Node>();

		while (cursor != null) {
			totalPath.add(cursor);
			cursor = cursor.getPrev();
		}

		return totalPath;
	}

	private static List<Node> getNeighbors(Node current) {
		List<Node> neighbors = new ArrayList<Node>();
		int x = current.getX();
		int y = current.getY();
		int neighborX;
		int neighborY;

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0) {
					continue;
				}

				neighborX = x + i;
				neighborY = y + j;
				if ((neighborX >= 0 && neighborX < xLength) && (neighborY >= 0 && neighborY < yLength)
						&& coordinateMatrix[neighborX][neighborY]) {
					neighbors.add(new Node(neighborX, neighborY));
				}
			}
		}

		return neighbors;
	}

}
