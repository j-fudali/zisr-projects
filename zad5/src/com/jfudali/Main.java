package com.jfudali;

import com.jfudali.airconditioner.AirConditioner;

public class Main {

  static void main() {
    AirConditioner airConditioner = new AirConditioner(23.0, 32.0);
    airConditioner.runConditioner();
  }
}
