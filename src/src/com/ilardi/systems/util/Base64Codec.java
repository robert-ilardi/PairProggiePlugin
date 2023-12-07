/**
 * Created Aug 28, 2006
 */

/*
 Copyright 2007 Robert C. Ilardi

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.ilardi.systems.util;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * @author Robert C. Ilardi
 *
 */

public class Base64Codec {

  //NOTE: '=' is NOT a digit, it is a special terminator value. It has the index of 64 (the 65th element) for fast conversions...
  public static final char[] DIGITS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
      'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/', '=' };

  public static final HashMap<Character, Integer> DIGITS_MAP = new HashMap<Character, Integer>();

  static {
    //Init HashMap of Base 64 Digits to Base 10 Integers
    DIGITS_MAP.put('A', 0);
    DIGITS_MAP.put('B', 1);
    DIGITS_MAP.put('C', 2);
    DIGITS_MAP.put('D', 3);
    DIGITS_MAP.put('E', 4);
    DIGITS_MAP.put('F', 5);
    DIGITS_MAP.put('G', 6);
    DIGITS_MAP.put('H', 7);
    DIGITS_MAP.put('I', 8);
    DIGITS_MAP.put('J', 9);
    DIGITS_MAP.put('K', 10);
    DIGITS_MAP.put('L', 11);
    DIGITS_MAP.put('M', 12);
    DIGITS_MAP.put('N', 13);
    DIGITS_MAP.put('O', 14);
    DIGITS_MAP.put('P', 15);
    DIGITS_MAP.put('Q', 16);
    DIGITS_MAP.put('R', 17);
    DIGITS_MAP.put('S', 18);
    DIGITS_MAP.put('T', 19);
    DIGITS_MAP.put('U', 20);
    DIGITS_MAP.put('V', 21);
    DIGITS_MAP.put('W', 22);
    DIGITS_MAP.put('X', 23);
    DIGITS_MAP.put('Y', 24);
    DIGITS_MAP.put('Z', 25);
    DIGITS_MAP.put('a', 26);
    DIGITS_MAP.put('b', 27);
    DIGITS_MAP.put('c', 28);
    DIGITS_MAP.put('d', 29);
    DIGITS_MAP.put('e', 30);
    DIGITS_MAP.put('f', 31);
    DIGITS_MAP.put('g', 32);
    DIGITS_MAP.put('h', 33);
    DIGITS_MAP.put('i', 34);
    DIGITS_MAP.put('j', 35);
    DIGITS_MAP.put('k', 36);
    DIGITS_MAP.put('l', 37);
    DIGITS_MAP.put('m', 38);
    DIGITS_MAP.put('n', 39);
    DIGITS_MAP.put('o', 40);
    DIGITS_MAP.put('p', 41);
    DIGITS_MAP.put('q', 42);
    DIGITS_MAP.put('r', 43);
    DIGITS_MAP.put('s', 44);
    DIGITS_MAP.put('t', 45);
    DIGITS_MAP.put('u', 46);
    DIGITS_MAP.put('v', 47);
    DIGITS_MAP.put('w', 48);
    DIGITS_MAP.put('x', 49);
    DIGITS_MAP.put('y', 50);
    DIGITS_MAP.put('z', 51);
    DIGITS_MAP.put('0', 52);
    DIGITS_MAP.put('1', 53);
    DIGITS_MAP.put('2', 54);
    DIGITS_MAP.put('3', 55);
    DIGITS_MAP.put('4', 56);
    DIGITS_MAP.put('5', 57);
    DIGITS_MAP.put('6', 58);
    DIGITS_MAP.put('7', 59);
    DIGITS_MAP.put('8', 60);
    DIGITS_MAP.put('9', 61);
    DIGITS_MAP.put('+', 62);
    DIGITS_MAP.put('/', 63);
    DIGITS_MAP.put('=', -1);
  }

  public static final int PADDING_INDEX = 64;

  //Start Encoding Code Block--------------------------------------------------------------------------->

  public static String Encode(byte[] data) {
    return Encode(data, true);
  }

  /**
   * 
   * Base 64 Encoding works off of a 24 bit buffer.
   * It basically converts 3 bytes into 4, since normal
   * ASCII binary data is really base 256, we are stepping down
   * to base 64, therefore the number of "digits" for a single
   * number increases.
   * 
   */
  public static String Encode(byte[] data, boolean newlines) {
    StringBuffer b64Data = new StringBuffer();
    int ascii, buf, padding, cnt;
    int[] b64Set;
    String b64Num;

    if (data != null) {
      cnt = 0; //MUST be set to 0 NOT 1 because the first iteration of the for i loop executes cnt++ before appending to the string buffer
      for (int i = 0; i < data.length; i += 3) {
        buf = 0;
        padding = 2;

        //First byte (will eventually become left most 8 bits in the 24-bit buffer)
        ascii = data[i] & 0xFF;
        buf = ascii;
        buf = buf << 8;

        //Second byte (will eventually become middle 8 bits in the 24-bit buffer) 
        if ((i + 1) < data.length) {
          ascii = data[i + 1] & 0xFF;
          buf = buf + ascii;
          padding--;
        }
        buf = buf << 8;

        //Third byte (will eventually become the right most 8 bits in the 24-bit buffer)
        if ((i + 2) < data.length) {
          ascii = data[i + 2] & 0xFF;
          buf = buf + ascii;
          padding--;
        }

        b64Set = ConvertToBase64Set(buf, padding); //Obtain Digit Indexes 0-63 normally, with a special 64th index for terminator value
        b64Num = ConvertToBase64Number(b64Set); //Get actual string reprentation of the 3 bytes as a base 64 number (4 bytes).

        //RFC 1421 and RFC 2045 requires a CR+LF newline every 76 characters OR ever 19 sets of 4 bytes base 64 numbers.
        if (cnt == 19) {
          cnt = 1; //Because the first iteration of the for i loop the ELSE block is executed instead, we set to 1 instead of 0.
          if (newlines) {
            b64Data.append("\r\n"); //CR+LF
          }
        }
        else {
          cnt++;
        }

        b64Data.append(b64Num);
      } //End for i loop through data byte array

      //Trailing CR+LF newline if required
      if (cnt == 19 && b64Data.charAt(b64Data.length() - 1) != '=') {
        b64Data.append("\r\n"); //CR+LF
      }

    } //End data != null check

    return b64Data.toString();
  }

  /**
   * Does the actual base 64 conversion math 
   */
  private static int[] ConvertToBase64Set(int b10Num, int padding) {
    int[] b64Set = new int[4];
    int dividend, quotient, remainder, cnt;
    final int divisor = 64;

    cnt = 3;
    dividend = b10Num;

    do {
      quotient = dividend / divisor;
      remainder = dividend % divisor;

      dividend = quotient;

      b64Set[cnt] = remainder;
      cnt--;
    } while (quotient > 0);

    for (int i = 0; i < padding; i++) {
      b64Set[3 - i] = PADDING_INDEX;
    }

    return b64Set;
  }

  /**
   * Simply does a table lookup for the correct digits
   */
  private static String ConvertToBase64Number(int[] b64Set) {
    StringBuffer b64Num = new StringBuffer();

    for (int i = 0; i < b64Set.length; i++) {
      b64Num.append(DIGITS[b64Set[i]]);
    }

    return b64Num.toString();
  }

  //Start Decoding Code Block--------------------------------------------------------------------------->

  public static byte[] Decode(String b64Data) {
    return Decode(b64Data, true);
  }

  public static byte[] Decode(String b64Data, boolean newlines) {
    ByteArrayOutputStream baos = null;
    byte[] data = null, tmp;
    int b64Val, buf, b1, b2, b3, cnt, padding;

    if (b64Data != null && b64Data.length() > 0) {
      cnt = 0;
      tmp = new byte[3];
      baos = new ByteArrayOutputStream();

      for (int i = 0; i < b64Data.length(); i += 4) {
        buf = 0;
        padding = 0;

        b64Val = DIGITS_MAP.get(b64Data.charAt(i));
        buf = b64Val * ((int) Math.pow(64, 3));

        b64Val = DIGITS_MAP.get(b64Data.charAt(i + 1));
        buf += b64Val * ((int) Math.pow(64, 2));

        b64Val = DIGITS_MAP.get(b64Data.charAt(i + 2));
        if (b64Val != -1) {
          buf += b64Val * ((int) Math.pow(64, 1));

          b64Val = DIGITS_MAP.get(b64Data.charAt(i + 3));
          if (b64Val != -1) {
            buf += b64Val; //same as b64Val * ((int) Math.pow(64, 0))
          } //End second -1 check
          else {
            padding = 1;
          }
        } //End first -1 check
        else {
          padding = 2;
        }

        //First Byte
        b1 = buf >> 16;
        tmp[0] = (byte) b1;

        //Second Byte
        if (padding != 2) {
          b2 = buf - (b1 << 16);
          b2 = b2 >> 8;
          tmp[1] = (byte) b2;

          //Third Byte
          if (padding == 0) {
            b3 = buf - ((b1 << 16) + (b2 << 8));
            tmp[2] = (byte) b3;
          } //End padding == 0 check
        } //End padding != 2 check

        baos.write(tmp, 0, 3 - padding);

        //RFC 1421 and RFC 2045 requires a CR+LF newline every 76 characters base 64 numbers.
        cnt += 4;

        if (cnt == 76) {
          cnt = 0;

          if (newlines) {
            i += 2; //Advance i to skip the CR+LF newline
          }
        }
      } //End for i loop through b64Data String

      data = baos.toByteArray();
    } //End b64Data null and length check

    return data;
  }

}
