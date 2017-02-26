package com.ajrod.duckhunt.objects;

// represents a unit vector that gives the direction needed
// approximating sqrt(2) as 0.7

public enum Direction {
    UpRight(new Point(0.7f, 0.7f)) {
        @Override
        public Direction[] nextValidDirections() {
            return leftDirections;
        }
    },

    UpLeft(new Point(-0.7f, 0.7f)) {
        @Override
        public Direction[] nextValidDirections() {
            return rightDirections;
        }
    },

    Right(new Point(1, 0)) {
        @Override
        public Direction[] nextValidDirections() {
            return leftDirections;
        }
    },

    Left(new Point(-1, 0)) {
        @Override
        public Direction[] nextValidDirections() {
            return rightDirections;
        }
    };

    private static Direction[] leftDirections = {Direction.Left, Direction.UpLeft};
    private static Direction[] rightDirections = {Direction.Right, Direction.UpRight};
    private Point unitVector;

    Direction(Point p) {
        unitVector = p;
    }

    public float deltaX() {
        return unitVector.x;
    }

    public float deltaY() {
        return unitVector.y;
    }

    public boolean isMovingRight() {
        return unitVector.x > 0;
    }

    public boolean isMovingUp() {
        return unitVector.y > 0;
    }

    public abstract Direction[] nextValidDirections();
}
