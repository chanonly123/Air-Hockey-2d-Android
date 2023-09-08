package com.chanonly123.airhockey2d;

public class Vector2D {
    float f4x;
    float f5y;

    Vector2D(float a, float b) {
        this.f4x = a;
        this.f5y = b;
    }

    Vector2D(Vector2D v) {
        this.f4x = v.f4x;
        this.f5y = v.f5y;
    }

    void set(float a, float b) {
        this.f4x = a;
        this.f5y = b;
    }

    float value() {
        return (float) Math.sqrt((double) ((this.f4x * this.f4x) + (this.f5y * this.f5y)));
    }

    void add(Vector2D v) {
        this.f4x += v.f4x;
        this.f5y += v.f5y;
    }

    Vector2D getAdd(Vector2D v) {
        return new Vector2D(this.f4x + v.f4x, this.f5y + v.f5y);
    }

    void sub(Vector2D v) {
        this.f4x -= v.f4x;
        this.f5y -= v.f5y;
    }

    Vector2D getSub(Vector2D v) {
        return new Vector2D(this.f4x - v.f4x, this.f5y - v.f5y);
    }

    void scalarMul(float d) {
        this.f4x *= d;
        this.f5y *= d;
    }

    Vector2D getScalarMul(float d) {
        return new Vector2D(this.f4x * d, this.f5y * d);
    }

    Vector2D getUnit() {
        float d = value();
        return new Vector2D(this.f4x / d, this.f5y / d);
    }

    void setUnit() {
        float d = value();
        this.f4x /= d;
        this.f5y /= d;
    }

    float dotProduct(Vector2D v) {
        return (this.f4x * v.f4x) + (this.f5y * v.f5y);
    }

    Vector2D getNormal() {
        return new Vector2D(this.f5y, -this.f4x);
    }
}
