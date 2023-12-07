/*
 * Created on Mar 23, 2004
 *
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

/**
 * @author rilardi
 * 
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class StringUtils {

  /**
   * This class contains some useful string utility methods
   */

  public StringUtils() {}

  public static String GetStackTraceString(Throwable t) {
    ByteArrayOutputStream baos = null;
    PrintStream ps = null;
    String temp = null;

    try {
      baos = new ByteArrayOutputStream();
      ps = new PrintStream(baos);

      t.printStackTrace(ps);
      temp = baos.toString();
    }
    catch (Exception ie) {
      t.printStackTrace();
    }
    finally {
      try {
        if (ps != null) {
          ps.close();
        }
      }
      catch (Exception ie) {}

      try {
        if (baos != null) {
          baos.close();
        }
      }
      catch (Exception ie) {}
    }

    return temp;
  }

  public static synchronized String GenerateYearlyUniqueKey() {
    StringBuffer uniqueId = new StringBuffer();

    String[] ids = TimeZone.getAvailableIDs(-5 * 60 * 60 * 1000);
    SimpleTimeZone est = new SimpleTimeZone(-5 * 60 * 60 * 1000, ids[0]);
    est.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
    est.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
    Calendar now = new GregorianCalendar(est);

    uniqueId.append(Integer.toString(now.get(Calendar.DAY_OF_YEAR), 36).toUpperCase().trim());
    uniqueId.append(Integer.toString(now.get(Calendar.HOUR_OF_DAY), 36).toUpperCase().trim());
    uniqueId.append(Integer.toString(now.get(Calendar.MINUTE), 36).toUpperCase().trim());
    uniqueId.append(Integer.toString(now.get(Calendar.SECOND), 36).toUpperCase().trim());

    try {
      Thread.sleep(1000);
    }
    catch (Exception e) {}

    return uniqueId.toString();
  }

  public static java.util.Date ParseDate(String dateStr, String dateFormat) throws ParseException {
    java.util.Date date;

    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    date = sdf.parse(dateStr);

    return date;
  }

  public static String[] Tokenize(String s, char delimiter) {
    return Tokenize(s, delimiter, false);
  }

  public static String[] Tokenize(String s, char delimiter, boolean keepEmpty) {
    ArrayList<String> al = new ArrayList<String>();
    String[] tempArr = null;
    StringBuffer sb = new StringBuffer();
    char c;
    int cnt = 0;

    for (int i = 0; i < s.length(); i++) {
      c = s.charAt(i);

      if (c == delimiter) {
        if (sb.length() > 0 || keepEmpty) {
          al.add(sb.toString());
          sb.setLength(0);
        }
      }
      else {
        sb.append(c);
      }
    }

    if (sb.length() > 0) {
      al.add(sb.toString());
      sb.setLength(0);
    }

    if (al.size() > 0) {
      tempArr = new String[al.size()];
      while (!al.isEmpty()) {
        tempArr[cnt++] = al.remove(0);
      }
    }

    return tempArr;
  }

  public static String QuickDateFormat(String format) {
    return QuickDateFormat((new java.util.Date()), format);
  }

  public static String QuickDateFormat(java.util.Date date, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(date);
  }

  public static void PrintArray(String name, String[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static void PrintArray(PrintStream printer, String name, String[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(arr[i]).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintList(String name, List<?> lst) {
    Iterator<?> iter;
    int cnt = 0;

    if (lst != null) {
      System.out.println("List \"" + name + "\" has " + lst.size() + " elements -");
      iter = lst.iterator();
      while (iter.hasNext()) {
        System.out.println(name + "[" + cnt + "] = " + iter.next());
        cnt++;
      }
    }
    else {
      System.out.println("List \"" + name + "\" is NULL!");
    }
  }

  public static void PrintMap(String name, Map<?, ?> map) {
    Set<?> keys;
    Iterator<?> iter;
    String key;
    Object obj;
    Object[] arr;

    if (map != null) {
      keys = map.keySet();
      iter = keys.iterator();

      System.out.println("Map \"" + name + "\" has " + map.size() + " elements -");

      while (iter.hasNext()) {
        key = (String) iter.next();
        obj = map.get(key);

        if (obj instanceof Object[]) {
          arr = (Object[]) obj;
          for (int i = 0; i < arr.length; i++) {
            System.out.println(key + "[" + i + "] = " + arr[i]);
          }
        }
        else {
          System.out.println(key + " = " + obj);
        }
      }
    }
    else {
      System.out.println("Map \"" + name + "\" is NULL!");
    }
  }

  public static String GetTimeStamp() {
    return (new java.util.Date()).toString();
  }

  public static String FormatDouble(double d, int precision) {
    BigDecimal bd = new BigDecimal(d);
    bd = bd.setScale(precision, BigDecimal.ROUND_DOWN);
    return "" + bd.toString();
  }

  /*
   * This method returns a String Array where each element is a single character
   * of the parameter "s."
   */
  public static String[] GetCharsAsStrings(String s) {
    String[] letters = null;

    if (s != null) {
      letters = new String[s.length()];
      letters[0] = "";
      for (int i = 0; i < s.length(); i++) {
        letters[i] = String.valueOf(s.charAt(i));
      }
    }

    return letters;
  }

  /*
   * This method return the boolean valud of the string. It returns true if and
   * only if the String vlaue is equal to "TRUE" The test is case insensitive,
   * and the string "s" is trimmed!
   */
  public static boolean GetBoolean(String s) {
    return s != null && s.trim().equalsIgnoreCase("TRUE");
  }

  /*
   * Generate some krapy Unique Id based on time and a few random Capital
   * Letters.
   */
  public static synchronized String GenerateTimeUniqueId() {
    StringBuffer id = new StringBuffer();

    // Some Random Character
    for (int i = 1; i <= 4; i++) {
      id.append((char) (65 + ((int) (26 * Math.random()))));
    }

    id.append(Long.toHexString(System.currentTimeMillis()).toUpperCase());

    try {
      Thread.sleep(10);
    }
    catch (Exception e) {}

    return id.toString();
  }

  public static String[] Trim(String[] arr) {
    String[] newArr = null;

    if (arr != null) {
      newArr = new String[arr.length];

      for (int i = 0; i < arr.length; i++) {
        newArr[i] = arr[i].trim();
      }
    }

    return newArr;
  }

  public static String ParseDir(String filePath) {
    String dirPath = null;
    int dirIndex;

    if (filePath != null && filePath.trim().length() > 0) {
      filePath = filePath.trim();
      dirIndex = filePath.lastIndexOf("/");

      if (dirIndex < 0) {
        dirIndex = filePath.lastIndexOf("\\");
      }

      if (dirIndex > 0) {
        dirPath = filePath.substring(0, dirIndex);
      }
      else if (dirIndex == 0 && filePath.startsWith("/")) {
        dirPath = "/";
      }
      else if (dirIndex == 0 && filePath.startsWith("\\")) {
        dirPath = "\\";
      }
      else {
        dirPath = "";
      }
    }

    return dirPath;
  }

  public static boolean ContainsOne(String s, String[] arr) {
    boolean contains = false;

    for (int i = 0; s != null && arr != null && i < arr.length; i++) {
      if (s.indexOf(arr[i]) >= 0) {
        contains = true;
        break;
      }
    }

    return contains;
  }

  public static boolean EqualsOne(String s, String[] arr, boolean caseInsensitive) {
    boolean equals = false;

    for (int i = 0; s != null && arr != null && i < arr.length; i++) {
      if (caseInsensitive && s.equalsIgnoreCase(arr[i])) {
        equals = true;
        break;
      }
      else if (!caseInsensitive && s.equals(arr[i])) {
        equals = true;
        break;
      }
    }

    return equals;
  }

  public static String Delimiterize(String[] tokens, String delimiter) {
    StringBuffer sb = new StringBuffer();

    for (int i = 0; tokens != null && i < tokens.length; i++) {
      if (i > 0) {
        sb.append(delimiter);
      }
      sb.append(tokens[i]);
    }

    return sb.toString();
  }

  /**
   * @deprecated Replaced by {@link #ApplyParameters(String, String, String[])}
   */
  public static String ApplyParameters(String text, String placeHolder, String placeHolderRegEx, String[] parameters) {
    String parameterizedText;
    int index, cnt;

    cnt = 0;
    parameterizedText = text;
    index = parameterizedText.indexOf(placeHolder);

    while (index >= 0 && parameters != null && cnt < parameters.length) {
      parameterizedText = parameterizedText.replaceFirst(placeHolderRegEx, parameters[cnt++]);
      index = parameterizedText.indexOf(placeHolder);
    }

    return parameterizedText;
  }

  public static String ApplyParameters(String text, String placeHolder, String[] parameters) {
    StringBuffer parameterizedText;
    int index, cnt;

    cnt = 0;
    parameterizedText = new StringBuffer(text);
    index = parameterizedText.indexOf(placeHolder);

    while (index >= 0 && parameters != null && cnt < parameters.length) {
      parameterizedText.replace(index, index + placeHolder.length(), parameters[cnt]);
      index = parameterizedText.indexOf(placeHolder, index + parameters[cnt].length());
      cnt++;
    }

    return parameterizedText.toString();
  }

  public static String[] Append(String[] record, String additionalValue) {
    String[] newRecord = null;

    if (record != null && record.length > 0) {
      newRecord = new String[record.length + 1];
      System.arraycopy(record, 0, newRecord, 0, record.length);
      newRecord[newRecord.length - 1] = additionalValue;
    }
    else {
      newRecord = new String[1];
      newRecord[0] = additionalValue;
    }

    return newRecord;
  }

  public static String[] Escape(String[] tokens, char escapee, char escapingChar) {
    String[] escapedArr = null;
    StringBuffer sb;

    if (tokens != null && tokens.length > 0) {
      escapedArr = new String[tokens.length];
      sb = new StringBuffer();

      for (int i = 0; i < tokens.length; i++) {
        sb.setLength(0);
        for (int j = 0; j < tokens[i].length(); j++) {
          if (tokens[i].charAt(j) == escapee) {
            sb.append(escapingChar);
          }
          sb.append(tokens[i].charAt(j));
        }
        escapedArr[i] = sb.toString();
      }
    }

    return escapedArr;
  }

  public static String HumanReadableTime(long milliSeconds) {
    long days, hours, inpSecs;
    int minutes, seconds;
    StringBuffer sb = new StringBuffer();

    inpSecs = milliSeconds / 1000; // Convert Milliseconds into Seconds
    days = inpSecs / 86400;
    hours = (inpSecs - (days * 86400)) / 3600;
    minutes = (int) (((inpSecs - (days * 86400)) - (hours * 3600)) / 60);
    seconds = (int) (((inpSecs - (days * 86400)) - (hours * 3600)) - (minutes * 60));

    sb.append(days);
    sb.append((days != 1 ? " Days" : " Day"));

    sb.append(", ");
    sb.append(hours);
    sb.append((hours != 1 ? " Hours" : " Hour"));

    sb.append(", ");
    sb.append(minutes);
    sb.append((minutes != 1 ? " Minutes" : " Minute"));

    sb.append(", ");
    sb.append(seconds);
    sb.append((seconds != 1 ? " Seconds" : " Second"));

    return sb.toString();
  }

  public static String StripHTML(String html) {
    StringBuffer text = new StringBuffer();
    boolean inTag = false;
    char c;

    for (int i = 0; i < html.length(); i++) {
      c = html.charAt(i);
      if (c == '<') {
        inTag = true;
      }
      else if (c == '>' && inTag) {
        inTag = false;
      }
      else if (!inTag) {
        text.append(c);
      }
    }

    return text.toString();
  }

  public static String[] QuoteSplit(String text, char splitChar) {
    String[] tempArr = null;
    ArrayList<String> al;
    StringBuffer sb;
    int cnt;
    boolean inQuotes;
    char c;

    if (text != null) {
      al = new ArrayList<String>();
      sb = new StringBuffer();
      inQuotes = false;

      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);
        if (c == '\"') {
          inQuotes = !inQuotes;
        }
        else if (!inQuotes && c == splitChar) {
          al.add(sb.toString());
          sb.setLength(0);
        }
        else {
          sb.append(c);
        }
      }

      if (sb.length() > 0) {
        al.add(sb.toString());
        sb.setLength(0);
      }

      cnt = 0;
      tempArr = new String[al.size()];
      while (!al.isEmpty()) {
        tempArr[cnt++] = al.remove(0);
      }
    } // End text != null check

    return tempArr;
  }

  public static boolean ContainsOnly(String text, String segment) {
    boolean only = false;
    int index = 0, lastIndex;

    if (text.length() > 0 && segment.length() > 0) {
      index = text.indexOf(segment);
      only = (index == 0);
      while (only && index != -1 && (index + segment.length()) < text.length()) {
        lastIndex = index;
        index = text.indexOf(segment, index + segment.length());
        only = (index == lastIndex + segment.length());
      }
    }
    else if (text.length() == segment.length()) {
      // Same as saving text.length()==0 && segment.length==0
      only = true;
    }

    return only;
  }

  public static String LTrim(String s) {
    String lts = null;
    char c;

    if (s != null) {
      lts = "";
      for (int i = 0; i < s.length(); i++) {
        c = s.charAt(i);
        if (c != ' ' && c != '\t') {
          lts = s.substring(i);
          break;
        }
      }
    }

    return lts;
  }

  public static String RTrim(String s) {
    String rts = null;
    char c;

    if (s != null) {
      rts = "";
      for (int i = s.length() - 1; i >= 0; i--) {
        c = s.charAt(i);
        if (c != ' ' && c != '\t') {
          rts = s.substring(0, i + 1);
          break;
        }
      }
    }

    return rts;
  }

  public static String[] ReadLines(InputStream ins) throws IOException {
    InputStreamReader isr = null;
    BufferedReader br = null;
    String[] lines = null;
    String line;
    ArrayList<String> al = null;
    int cnt;

    try {
      isr = new InputStreamReader(ins);
      br = new BufferedReader(isr);
      al = new ArrayList<String>();

      line = br.readLine();
      while (line != null) {
        line = line.trim();
        if (line.length() > 0) {
          al.add(line);
        }
        line = br.readLine();
      }

      cnt = 0;
      lines = new String[al.size()];
      while (!al.isEmpty()) {
        lines[cnt++] = al.remove(0);
      }
    }
    finally {
      if (isr != null) {
        try {
          isr.close();
        }
        catch (Exception e) {}
        isr = null;
      }

      if (br != null) {
        try {
          br.close();
        }
        catch (Exception e) {}
        br = null;
      }

      if (al != null) {
        al.clear();
        al = null;
      }
    }

    return lines;
  }

  public static String GetContent(InputStream ins) throws IOException {
    InputStreamReader isr = null;
    BufferedReader br = null;
    StringBuffer content = new StringBuffer();
    String line;

    try {
      isr = new InputStreamReader(ins);
      br = new BufferedReader(isr);

      line = br.readLine();
      while (line != null) {
        content.append(line);
        line = br.readLine();
      }
    }
    finally {
      if (isr != null) {
        try {
          isr.close();
        }
        catch (Exception e) {}
        isr = null;
      }

      if (br != null) {
        try {
          br.close();
        }
        catch (Exception e) {}
        br = null;
      }
    }

    return content.toString();
  }

  public static Properties ParseInlineProperties(String inlinePropsStr) {
    Properties props = new Properties();
    String[] nvPairs, nvPair;

    if (inlinePropsStr != null && inlinePropsStr.trim().length() > 0) {
      nvPairs = inlinePropsStr.split(";");

      if (nvPairs != null && nvPairs.length > 0) {
        nvPairs = Trim(nvPairs);

        for (int i = 0; i < nvPairs.length; i++) {
          nvPair = nvPairs[i].split("=", 2);
          if (nvPair != null && nvPair.length == 2) {
            nvPair = Trim(nvPair);
            props.setProperty(nvPair[0], nvPair[1]);
          }
        }
      }
    }

    return props;
  }

  public static String BasicURLEncode(String ps) {
    String es = null;

    try {
      es = URLEncoder.encode(ps, "UTF-8");
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return es;
  }

  public static String BasicURLDecode(String es) {
    String ps = null;

    try {
      ps = URLDecoder.decode(es, "UTF-8");
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return ps;
  }

  public static String LPad(String s, char paddingChar, int fixedLen) {
    StringBuffer sb = new StringBuffer();

    for (int i = 1; i <= fixedLen - (s != null ? s.length() : 0); i++) {
      sb.append(paddingChar);
    }

    if (s != null) {
      sb.append(s);
    }

    return sb.toString();
  }

  public static String RPad(String s, char paddingChar, int fixedLen) {
    StringBuffer sb = new StringBuffer();

    if (s != null) {
      sb.append(s);
    }

    for (int i = 1; i <= fixedLen - (s != null ? s.length() : 0); i++) {
      sb.append(paddingChar);
    }

    return sb.toString();
  }

  public static String ArrayToDelimitedString(String[] arr, String delimiter, boolean trailingDelimiter) {
    StringBuffer sb = new StringBuffer();

    if (arr != null) {
      for (int i = 0; i < arr.length; i++) {
        if (i > 0) {
          sb.append(delimiter);
        }

        sb.append(arr[i]);
      }
    }

    if (sb.length() > 0 && trailingDelimiter) {
      sb.append(delimiter);
    }

    return sb.toString();
  }

  public static Properties PrefixPropNames(Properties props, String prefix) {
    Properties prefixedProps = null;
    Iterator<Object> iter;
    String name, value;
    StringBuffer prefixedName;

    if (props != null) {
      prefixedProps = new Properties();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        prefixedName = new StringBuffer(prefix);
        prefixedName.append(name);

        prefixedProps.setProperty(prefixedName.toString(), value);

        prefixedName = null;
      }
    }

    return prefixedProps;
  }

  public static String HtmlCharacterEncoder(String text) {
    StringBuffer html = new StringBuffer();
    int ascii;
    char c;

    if (text != null) {
      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);
        ascii = (int) c;

        if ((ascii < 48 || ascii > 57) && (ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122) && ascii != 32) {
          html.append("&#");

          if (ascii < 10) {
            html.append("00");
          }
          else if (ascii < 100) {
            html.append("0");
          }

          html.append(ascii);
          html.append(";");
        }
        else {
          html.append(c);
        }
      }
    }

    return html.toString();
  }

  public static String HtmlCharacterEncoderIgnoreNewLines(String text) {
    StringBuffer html = new StringBuffer();
    int ascii;
    char c;

    if (text != null) {
      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);
        ascii = (int) c;

        if ((ascii < 48 || ascii > 57) && (ascii < 65 || ascii > 90) && (ascii < 97 || ascii > 122) && ascii != 32 && ascii != 13 && ascii != 10) {
          html.append("&#");

          if (ascii < 10) {
            html.append("00");
          }
          else if (ascii < 100) {
            html.append("0");
          }

          html.append(ascii);
          html.append(";");
        }
        else {
          html.append(c);
        }
      }
    }

    return html.toString();
  }

  public static String HtmlCharacterDecoder(String html) {
    StringBuffer text = new StringBuffer();
    char c;
    String temp;

    if (html != null) {
      for (int i = 0; i < html.length(); i++) {
        c = html.charAt(i);

        if (c == '&') {
          temp = html.substring(i + 2, i + 5);
          text.append((char) Integer.parseInt(temp));
          i += 5;
        }
        else {
          text.append(c);
        }
      }
    }

    return text.toString();
  }

  public static String SimpleXMLTextEncoder(String text) {
    StringBuffer html = new StringBuffer();
    char c;

    if (text != null) {
      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);

        switch (c) {
          case '<':
            html.append("&lt;");
            break;
          case '>':
            html.append("&gt;");
            break;
          case '&':
            html.append("&amp;");
            break;
          case '\"':
            html.append("&quot;");
            break;
          case '\'':
            html.append("&apos;");
            break;
          default:
            html.append(c);
        }
      }
    }

    return html.toString();
  }

  public static boolean IsNVL(String s) {
    return s == null || s.trim().length() == 0;
  }

  public static String[] FixedLengthSplit(String str, int[] lens) {
    String[] tokens = null;
    int startIndex = 0, endIndex = 0, sLen;

    if (str != null && (sLen = str.length()) > 0 && lens != null && lens.length > 0) {

      tokens = new String[lens.length];

      for (int i = 0; i < lens.length && startIndex < sLen; i++) {
        endIndex = startIndex + lens[i];

        if (endIndex <= sLen) {
          tokens[i] = str.substring(startIndex, endIndex);
        }
        else {
          tokens[i] = str.substring(startIndex);
        }

        startIndex = endIndex;
      } // End for i loop
    } // End input param checks

    return tokens;
  }

  public static String PrepareFixedLengthRecord(String[] tokens, char[] fieldPaddings, boolean[] padLeftDirections, int[] fieldLens, boolean appendNewLine) {
    StringBuffer record = new StringBuffer();

    if (tokens != null && fieldPaddings != null && fieldPaddings.length != 0 && fieldLens != null && fieldPaddings.length == fieldLens.length && padLeftDirections != null
        && fieldPaddings.length == padLeftDirections.length) {
      for (int i = 0; i < fieldLens.length; i++) {
        if (padLeftDirections[i]) {
          // Pad Left
          record.append(LPad((i < tokens.length ? tokens[i] : ""), fieldPaddings[i], fieldLens[i]));
        }
        else {
          // Pad Right
          record.append(RPad((i < tokens.length ? tokens[i] : ""), fieldPaddings[i], fieldLens[i]));
        }
      } // End for i loop

      if (appendNewLine) {
        record.append("\n");
      }
    } // End input param checks

    return record.toString();
  }

  public static String ParseFileFromPath(String filePath) {
    String filename = null;
    int dirIndex;

    if (filePath != null && filePath.trim().length() > 0) {
      filePath = filePath.trim();
      dirIndex = filePath.lastIndexOf("/");

      if (dirIndex < 0) {
        dirIndex = filePath.lastIndexOf("\\");
      }

      if (dirIndex >= 0) {
        filename = filePath.substring(dirIndex + 1, filePath.length());
      }
      else {
        filename = filePath;
      }
    }

    return filename;
  }

  public static String[] SplitFilePath(String filePath) {
    String[] arr = null;
    String dirPath, filename;

    if (filePath != null && filePath.trim().length() > 0) {
      dirPath = ParseDir(filePath);
      filename = ParseFileFromPath(filePath);

      arr = new String[2];
      arr[0] = dirPath;
      arr[1] = filename;
    }

    return arr;
  }

  public static synchronized String GetRandomChars(int count) {
    StringBuffer buf = new StringBuffer();

    // Some Random Character
    for (int i = 1; i <= count; i++) {
      if (((int) (100 * Math.random())) >= 50) {
        // Numbers
        buf.append((char) (48 + ((int) (10 * Math.random()))));
      }
      else {
        // Letters
        buf.append((char) (65 + ((int) (26 * Math.random()))));
      }
    }

    return buf.toString();
  }

  public static boolean IsNumeric(String s) {
    boolean numeric = false;
    char c;

    if (!IsNVL(s)) {
      numeric = true;
      s = s.trim();

      for (int i = 0; i < s.length(); i++) {
        c = s.charAt(i);

        if (i == 0 && (c == '-' || c == '+')) {
          // Ignore signs...
          continue;
        }
        else if (c < '0' || c > '9') {
          numeric = false;
          break;
        }
      }
    }

    return numeric;
  }

  public static boolean IsDouble(String s) {
    boolean numeric = false;
    char c;
    boolean foundDecimal = false;

    if (!IsNVL(s)) {
      numeric = true;
      s = s.trim();

      for (int i = 0; i < s.length(); i++) {
        c = s.charAt(i);

        if (i == 0 && (c == '-' || c == '+')) {
          // Ignore signs...
          continue;
        }
        else if (i != 0 && !foundDecimal && c == '.') {
          foundDecimal = true;
          continue;
        }
        else if (c < '0' || c > '9') {
          numeric = false;
          break;
        }
      }
    }

    return numeric;
  }

  public static String[] ReadLinesIgnoreComments(InputStream ins, String commentPrefix) throws IOException {
    InputStreamReader isr = null;
    BufferedReader br = null;
    String[] lines = null;
    String line;
    ArrayList<String> al = null;
    int cnt;

    try {
      isr = new InputStreamReader(ins);
      br = new BufferedReader(isr);
      al = new ArrayList<String>();

      line = br.readLine();
      while (line != null) {
        line = line.trim();
        if (line.length() > 0 && !line.startsWith(commentPrefix)) {
          al.add(line);
        }
        line = br.readLine();
      }

      cnt = 0;
      lines = new String[al.size()];
      while (!al.isEmpty()) {
        lines[cnt++] = al.remove(0);
      }
    }
    finally {
      if (isr != null) {
        try {
          isr.close();
        }
        catch (Exception e) {}
        isr = null;
      }

      if (br != null) {
        try {
          br.close();
        }
        catch (Exception e) {}
        br = null;
      }

      if (al != null) {
        al.clear();
        al = null;
      }
    }

    return lines;
  }

  public static String[] RemoveEmpties(String[] arr) {
    String[] neArr = null;
    ArrayList<String> al;

    if (arr != null) {
      al = new ArrayList<String>();

      for (int i = 0; i < arr.length; i++) {
        if (!IsNVL(arr[i])) {
          al.add(arr[i]);
        }
      }

      neArr = new String[al.size()];
      neArr = (String[]) al.toArray(neArr);
      al.clear();
      al = null;
    }

    return neArr;
  }

  public static boolean InArray(char c, char[] arr) {
    boolean contains = false;

    for (int i = 0; arr != null && i < arr.length; i++) {
      if (c == arr[i]) {
        contains = true;
        break;
      }
    }

    return contains;
  }

  public static boolean InArray(String s, String[] arr) {
    boolean contains = false;

    for (int i = 0; s != null && arr != null && i < arr.length; i++) {
      if (s.equals(arr[i])) {
        contains = true;
        break;
      }
    }

    return contains;
  }

  public static String toMultiLineString(Properties props) {
    StringBuffer sb = new StringBuffer();
    String name, prop;
    Iterator<Object> iter;

    if (props != null) {
      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        prop = props.getProperty(name);

        sb.append(name.trim());
        sb.append("=");
        sb.append(prop.trim());
        sb.append("\n");
      }
    }

    return sb.toString();
  }

  public static String GetTrailingInteger(String s) {
    StringBuffer endNum = new StringBuffer();
    char c;

    if (s != null) {
      for (int i = s.length() - 1; i >= 0; i--) {
        c = s.charAt(i);

        if (c >= '0' && c <= '9') {
          endNum.insert(0, c);
        }
        else {
          break;
        }
      }
    }

    return endNum.toString();
  }

  public static String[] GetToStringArray(Object[] arr) {
    String[] sArr = null;

    if (arr != null) {
      sArr = new String[arr.length];

      for (int i = 0; i < sArr.length; i++) {
        if (arr[i] != null) {
          sArr[i] = arr[i].toString();
        }
      }
    }

    return sArr;
  }

  public static String GetNextWord(String stmt, int startPos) {
    StringBuffer sb = new StringBuffer();
    char ch;
    int i;
    String nextWord;

    // Find first non white-space character
    for (i = startPos; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (ch != ' ') {
        break;
      }
    }

    // Read next word
    for (; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (ch == ' ') {
        break;
      }
      else {
        sb.append(ch);
      }
    }

    nextWord = sb.toString().trim();

    return nextWord;
  }

  public static int GetPositionAfterNextWord(String stmt, int startPos) {
    StringBuffer sb = new StringBuffer();
    char ch;
    int i;

    // Find first non white-space character
    for (i = startPos; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (ch != ' ') {
        break;
      }
    }

    // Read next word
    for (; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (ch == ' ') {
        break;
      }
      else {
        sb.append(ch);
      }
    }

    return i;
  }

  public static boolean CharIn(char ch, char[] delimiters) {
    boolean found = false;

    for (char d : delimiters) {
      if (ch == d) {
        found = true;
        break;
      }
    }

    return found;
  }

  public static String GetNextWord(String stmt, int startPos, char[] delimiters) {
    StringBuffer sb = new StringBuffer();
    char ch;
    int i;
    String nextWord;

    // Find first non delimiter character
    for (i = startPos; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (!CharIn(ch, delimiters)) {
        break;
      }
    }

    // Read next word
    for (; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (CharIn(ch, delimiters)) {
        break;
      }
      else {
        sb.append(ch);
      }
    }

    nextWord = sb.toString().trim();

    return nextWord;
  }

  public static int GetPositionAfterNextWord(String stmt, int startPos, char[] delimiters) {
    StringBuffer sb = new StringBuffer();
    char ch;
    int i;

    // Find first non delimiter character
    for (i = startPos; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (!CharIn(ch, delimiters)) {
        break;
      }
    }

    // Read next word
    for (; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (CharIn(ch, delimiters)) {
        break;
      }
      else {
        sb.append(ch);
      }
    }

    return i;
  }

  public static String GetNextWordNegativeDelimiter(String stmt, int startPos, char[] delimiters) {
    StringBuffer sb = new StringBuffer();
    char ch;
    int i;
    String nextWord;

    // Find first delimiter character
    for (i = startPos; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (CharIn(ch, delimiters)) {
        break;
      }
    }

    // Read next word
    for (; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (!CharIn(ch, delimiters)) {
        break;
      }
      else {
        sb.append(ch);
      }
    }

    nextWord = sb.toString().trim();

    return nextWord;
  }

  public static int GetPositionAfterNextWordNegativeDelimiter(String stmt, int startPos, char[] delimiters) {
    StringBuffer sb = new StringBuffer();
    char ch;
    int i;

    // Find first delimiter character
    for (i = startPos; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (CharIn(ch, delimiters)) {
        break;
      }
    }

    // Read next word
    for (; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (!CharIn(ch, delimiters)) {
        break;
      }
      else {
        sb.append(ch);
      }
    }

    return i;
  }

  public static String[] SingleQuoteSplit(String text, char splitChar) {
    String[] tempArr = null;
    ArrayList<String> al;
    StringBuffer sb;
    int cnt;
    boolean inQuotes;
    char c;

    if (text != null) {
      al = new ArrayList<String>();
      sb = new StringBuffer();
      inQuotes = false;

      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);
        if (c == '\'') {
          inQuotes = !inQuotes;
        }
        else if (!inQuotes && c == splitChar) {
          al.add(sb.toString());
          sb.setLength(0);
        }
        else {
          sb.append(c);
        }
      }

      if (sb.length() > 0) {
        al.add(sb.toString());
        sb.setLength(0);
      }

      cnt = 0;
      tempArr = new String[al.size()];
      while (!al.isEmpty()) {
        tempArr[cnt++] = al.remove(0);
      }
    } // End text != null check

    return tempArr;
  }

  public static String GetNextWordRespectSingleQuotes(String stmt, int startPos, char escapeChar) {
    StringBuffer sb = new StringBuffer();
    char ch;
    int i;
    String nextWord;
    boolean inQuotes = false, escaped = false;

    // Find first non white-space character
    for (i = startPos; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (ch != ' ') {
        break;
      }
    }

    // Read next word
    for (; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (ch == ' ' && !inQuotes) {
        break;
      }
      else if (ch == '\'' && !escaped) {
        inQuotes = !inQuotes;
      }
      else if (!escaped && ch == escapeChar) {
        escaped = true;
      }
      else {
        escaped = false;
        sb.append(ch);
      }
    }

    nextWord = sb.toString().trim();

    return nextWord;
  }

  public static int GetPositionAfterNextWordRespectSingleQuotes(String stmt, int startPos, char escapeChar) {
    StringBuffer sb = new StringBuffer();
    char ch;
    int i;
    boolean inQuotes = false, escaped = false;

    // Find first non white-space character
    for (i = startPos; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (ch != ' ') {
        break;
      }
    }

    // Read next word
    for (; i < stmt.length(); i++) {
      ch = stmt.charAt(i);

      if (ch == ' ' && !inQuotes) {
        break;
      }
      else if (ch == '\'' && !escaped) {
        inQuotes = !inQuotes;
      }
      else if (!escaped && ch == escapeChar) {
        escaped = true;
      }
      else {
        escaped = false;
        sb.append(ch);
      }
    }

    return i;
  }

  public static char GetFirstNonWhiteSpaceChar(String s, int startPos) {
    char ch = '\0';

    // Find first non white-space character
    for (int i = startPos; i < s.length(); i++) {
      ch = s.charAt(i);

      if (ch != ' ') {
        break;
      }
    }

    return ch;
  }

  public static boolean EndsWithOne(String s, String[] suffix, boolean ignoreCase) {
    boolean ends = false;

    for (int i = 0; i < suffix.length; i++) {
      if (ignoreCase) {
        ends = s.toUpperCase().endsWith(suffix[i].toUpperCase());
      }
      else {
        ends = s.endsWith(suffix[i]);
      }

      if (ends) {
        break;
      }
    }

    return ends;
  }

  public static String SimpleHTMLTextEncoder(String text) {
    StringBuffer html = new StringBuffer();
    char c;

    if (text != null) {
      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);

        switch (c) {
          case '<':
            html.append("&lt;");
            break;
          case '>':
            html.append("&gt;");
            break;
          case '&':
            html.append("&amp;");
            break;
          case '\"':
            html.append("&quot;");
            break;
          default:
            html.append(c);
        }
      }
    }

    return html.toString();
  }

  public static String Combine(String[] tokens) {
    return Combine(tokens, tokens.length);
  }

  public static String Combine(String[] tokens, int len) {
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < len && i < tokens.length; i++) {
      sb.append(tokens);
    }

    return sb.toString();
  }

  public static String CombineWithDelimiter(String[] tokens, String delimiter, int len, boolean startingDelimiter, boolean trailingDelimiter) {
    StringBuffer sb = new StringBuffer();

    if (startingDelimiter) {
      sb.append(delimiter);
    }

    for (int i = 0; i < len && i < tokens.length; i++) {
      if (i > 0) {
        sb.append(delimiter);
      }

      sb.append(tokens[i]);
    }

    if (trailingDelimiter) {
      sb.append(delimiter);
    }

    return sb.toString();
  }

  public static String[] SmartSplit(String s, char delimiter, char escapeChar) {
    String[] arr = null;
    ArrayList<String> al;
    int cnt;
    StringBuffer sb;
    char ch;
    boolean escape = false;

    if (s != null) {
      sb = new StringBuffer();
      al = new ArrayList<String>();

      for (int i = 0; i < s.length(); i++) {
        ch = s.charAt(i);

        if (escape) {
          sb.append(ch);
          escape = false;
        }
        else if (ch == escapeChar) {
          escape = true;
        }
        else if (ch == delimiter) {
          al.add(sb.toString());
          sb = new StringBuffer();
        }
        else {
          sb.append(ch);
        }
      }

      // Flush Buffer
      if (sb != null) {
        if (sb.length() > 0) {
          al.add(sb.toString());
        }
        sb = null;
      }

      cnt = 0;
      arr = new String[al.size()];
      while (!al.isEmpty()) {
        arr[cnt++] = (String) al.remove(0);
      }
    } // End defaultValuesStr NULL check

    return arr;
  }

  public static String[] SplitAtLen(String s, int maxLen) {
    String[] parts = null;
    int partCnt, startIndex, endIndex;

    if (s != null) {
      partCnt = s.length() / maxLen;
      if ((partCnt * maxLen) < s.length()) {
        partCnt++;
      }

      parts = new String[partCnt];

      for (int i = 0; i < partCnt; i++) {
        startIndex = (i * maxLen);

        if (i < (partCnt - 1)) {
          endIndex = startIndex + maxLen;
        }
        else {
          endIndex = s.length();
        }

        parts[i] = s.substring(startIndex, endIndex);
      }
    }

    return parts;
  }

  public static String Lineize(String plainText) {
    StringBuffer encodedText = new StringBuffer();

    if (plainText != null) {
      for (int i = 0; i < plainText.length(); i++) {
        if (plainText.charAt(i) == '\\') {
          encodedText.append("\\\\");
        }
        else if (plainText.charAt(i) == '\n') {
          encodedText.append("\\n");
        }
        else {
          encodedText.append(plainText.charAt(i));
        }
      }
    }

    return encodedText.toString();
  }

  public static String Delineize(String encodedText) {
    StringBuffer plainText = new StringBuffer();

    if (encodedText != null) {
      for (int i = 0; i < encodedText.length(); i++) {
        if (encodedText.charAt(i) == '\\' && i + 1 < encodedText.length()) {
          if (encodedText.charAt(i + 1) == '\\') {
            plainText.append("\\");
          }
          else if (encodedText.charAt(i + 1) == 'n') {
            plainText.append("\n");
          }
          else {
            plainText.append(encodedText.charAt(i + 1));
          }
          i++;
        }
        else {
          plainText.append(encodedText.charAt(i));
        }
      }
    }

    return plainText.toString();
  }

  public static String StripNewLines(String tmp) {
    StringBuffer sb;

    if (tmp != null) {
      sb = new StringBuffer();

      for (int i = 0; i < tmp.length(); i++) {
        if (tmp.charAt(i) != '\n') {
          sb.append(tmp.charAt(i));
        }
      }

      return sb.toString();
    }
    else {
      return null;

    }
  }

  public static boolean IsAlphaNumericOrSpace(String str) {
    boolean ret = true;

    for (int i = 0; i < str.length(); i++) {
      if (Character.isLetterOrDigit(str.charAt(i)) || Character.isSpaceChar(str.charAt(i))) {
        continue;
      }
      else {
        ret = false;
        break;
      }
    }

    return ret;
  }

  public static String ReadTextFile(InputStream ins) throws IOException {
    byte[] buf;
    String txt;

    buf = SystemUtils.LoadDataFromStream(ins);
    txt = new String(buf, 0, buf.length);

    return txt;
  }

  public static synchronized String GenerateTimeUniqueIdNumsOnly() {
    StringBuffer id = new StringBuffer();

    // Some Random Character
    for (int i = 1; i <= 4; i++) {
      id.append((char) (48 + ((int) (10 * Math.random()))));
    }

    id.append(System.currentTimeMillis());

    try {
      Thread.sleep(10);
    }
    catch (Exception e) {}

    return id.toString();
  }

  public static void PrintArray(String name, int[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static void PrintArray(PrintStream printer, String name, int[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(arr[i]).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintArray(String name, float[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static void PrintArray(PrintStream printer, String name, float[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(arr[i]).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintArray(String name, double[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static void PrintArray(PrintStream printer, String name, double[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(arr[i]).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintArray(String name, short[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static void PrintArray(PrintStream printer, String name, short[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(arr[i]).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintArray(String name, long[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static void PrintArray(PrintStream printer, String name, long[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(arr[i]).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintArray(String name, char[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static void PrintArray(PrintStream printer, String name, char[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(arr[i]).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintArray(String name, byte[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static void PrintArray(PrintStream printer, String name, byte[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(SystemUtils.GetAsciiFromByte(arr[i])).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintArray(PrintStream printer, String name, boolean[] arr) {
    if (arr != null) {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" has ").append(arr.length).append(" elements -").toString());
      for (int i = 0; i < arr.length; i++) {
        printer.println((new StringBuffer()).append(name).append("[").append(i).append("] = ").append(arr[i]).toString());
      }
    }
    else {
      printer.println((new StringBuffer()).append("Array \"").append(name).append("\" is NULL!").toString());
    }
  }

  public static void PrintArray(String name, boolean[] arr) {
    PrintArray(System.out, name, arr);
  }

  public static Properties GetPrefixedPropNames(Properties props, String prefix) {
    Properties prefixedProps = null;
    Iterator<Object> iter;
    String name, value;

    if (props != null) {
      prefixedProps = new Properties();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        if (name.startsWith(prefix)) {
          prefixedProps.setProperty(name, value);
        }
      }
    }

    return prefixedProps;
  }

  public static String[] ToStringArray(Object[] objArr) {
    String[] tsArr = null;

    if (objArr != null) {
      tsArr = new String[objArr.length];

      for (int i = 0; i < objArr.length; i++) {
        tsArr[i] = objArr[i].toString();
      }
    }

    return tsArr;
  }

  public static String[] Prepend(String[] record, String additionalValue) {
    String[] newRecord = null;

    if (record != null && record.length > 0) {
      newRecord = new String[record.length + 1];
      System.arraycopy(record, 0, newRecord, 1, record.length);
      newRecord[0] = additionalValue;
    }
    else {
      newRecord = new String[1];
      newRecord[0] = additionalValue;
    }

    return newRecord;
  }

  public static String[] Append(String[] record1, String[] record2) {
    String[] newRecord = null;

    if (record1 != null && record1.length > 0 && record2 != null && record2.length > 0) {
      newRecord = new String[record1.length + record2.length];
      System.arraycopy(record1, 0, newRecord, 0, record1.length);
      System.arraycopy(record2, 0, newRecord, record1.length, record2.length);
    }

    return newRecord;
  }

  public static String CompactHumanReadableTime(long milliSeconds) {
    long days, hours, inpSecs;
    int minutes, seconds;
    StringBuffer sb = new StringBuffer();

    inpSecs = milliSeconds / 1000; // Convert Milliseconds into Seconds
    days = inpSecs / 86400;
    hours = (inpSecs - (days * 86400)) / 3600;
    minutes = (int) (((inpSecs - (days * 86400)) - (hours * 3600)) / 60);
    seconds = (int) (((inpSecs - (days * 86400)) - (hours * 3600)) - (minutes * 60));

    if (days > 0) {
      sb.append(days);
      sb.append((days != 1 ? " Days" : " Day"));
    }

    if (sb.length() > 0) {
      sb.append(", ");
    }

    if (hours > 0 || sb.length() > 0) {
      sb.append(hours);
      sb.append((hours != 1 ? " Hours" : " Hour"));
    }

    if (sb.length() > 0) {
      sb.append(", ");
    }

    if (minutes > 0 || sb.length() > 0) {
      sb.append(minutes);
      sb.append((minutes != 1 ? " Minutes" : " Minute"));
    }

    if (sb.length() > 0) {
      sb.append(", ");
    }

    sb.append(seconds);
    sb.append((seconds != 1 ? " Seconds" : " Second"));

    return sb.toString();
  }

  public static synchronized String GenerateTimeUniqueIdNoDelay() {
    StringBuffer id = new StringBuffer();

    // Some Random Character
    for (int i = 1; i <= 8; i++) {
      id.append((char) (65 + ((int) (26 * Math.random()))));
    }

    id.append(Long.toHexString(System.currentTimeMillis()).toUpperCase());

    return id.toString();
  }

  public static void PrintArray(String name, Object[] arr) {

    if (arr != null) {
      System.out.println("Array \"" + name + "\" has " + arr.length + " elements -");
      for (int i = 0; i < arr.length; i++) {
        System.out.println(name + "[" + i + "] = " + arr[i]);
      }
    }
    else {
      System.out.println("Array \"" + name + "\" is NULL!");
    }
  }

  public static String FormatFloat(float f, int precision) {
    return FormatDouble((double) f, precision);
  }

  public static String removeChar(String inStr, char removed) {
    if (inStr == null)
      return null;
    if (inStr.indexOf(removed) < 0) // does not contains removed
      return inStr;
    int i = 0;
    int j = 0;
    String outStr = "";
    while (inStr.length() > i && inStr.substring(i).indexOf(removed) >= 0) {
      j = inStr.substring(i).indexOf(removed);
      outStr += inStr.substring(i, j + i);
      i += j + 1;
    }
    if (inStr.length() > i)
      outStr += inStr.substring(i);
    return outStr;

  }

  public static String padL(String str, int len, String filling) {
    String tmp = "";
    for (int x = 1; x <= len; x++)
      tmp += filling;

    tmp += str;

    return tmp.substring(tmp.length() - len, tmp.length());
  }

  public static String padR(String str, int len, String filling) {
    String tmp = "";
    for (int x = 1; x <= len; x++)
      tmp += filling;

    tmp = str + tmp;

    return tmp.substring(0, len);
  }

  public static boolean isNumeric(String s) {
    boolean isNumeric = true;
    try {
      Integer.parseInt(s);
    }
    catch (NumberFormatException nfe) {
      isNumeric = false;
    }
    return isNumeric;
  }

  public static String replaceStr(String origStr, String newToken, String oldToken) {
    String retStr = "";
    for (int i = 0, j = 0; (j = origStr.indexOf(oldToken, i)) > -1; i = j + oldToken.length()) {
      retStr += (origStr.substring(i, j) + newToken);
    }
    return (origStr.indexOf(oldToken) == -1) ? origStr : retStr + origStr.substring(origStr.lastIndexOf(oldToken) + oldToken.length(), origStr.length());
  }

  public static String EnsureNotNull(String str) {
    return str == null ? "" : str;
  }

  public static int parseIntDef(String s, int d) {
    int x;

    if (s == null)
      x = d;
    else if (IsNVL(s)) {
      x = d;
    }
    else {
      try {
        x = Integer.parseInt(s);
      }
      catch (NumberFormatException e) {
        System.out.println("ENAFUtils.parseIntDef: Integer.parseInt(" + s + ") conversion failed!!!  Using default [" + d + "].");
        // e.printStackTrace();
        x = d;
      }
    }
    return x;
  }

  public static Integer valueOfDef(String s, Integer d) {
    Integer x;

    if (s.trim().length() == 0) {
      x = d;
    }
    else {
      try {
        x = Integer.valueOf(s);
      }
      catch (NumberFormatException e) {
        System.out.println("Exception: Integer.valueOf(" + s + ") conversion failed!!!");
        e.printStackTrace();
        x = d;
      }
    }
    return x;
  }

  public static String stringValueOfDef(Integer d, String defVal) {
    String retVal = defVal;

    try {
      if (d == null)
        return retVal;

      retVal = EnsureNotNull(d.toString()).trim();
    }
    catch (Exception e) {
      System.out.println("Exception: stringValueOfDef conversion failed!!!");
      e.printStackTrace();
      retVal = defVal;
    }
    return retVal;
  }

  public static boolean hasAlpha(String str) {
    boolean ret = false;

    for (int i = 0; i < str.length(); i++) {
      if (Character.isLetter(str.charAt(i))) {
        ret = true;
        break;
      }
    }
    return ret;
  }

  public static Double doubleOfDef(String s, java.lang.Double dd) {
    java.lang.Double x;

    if (s.trim().length() == 0) {
      x = dd;
    }
    else {
      try {
        x = java.lang.Double.valueOf(s);
      }
      catch (NumberFormatException e) {
        System.out.println("Exception: Double.valueOf(" + s + ") conversion failed!!!");
        e.printStackTrace();
        x = dd;
      }
    }
    return x;
  }

  public static boolean isAlphaNumericSpace(String str) {
    boolean ret = true;

    for (int i = 0; i < str.length(); i++) {
      if (Character.isLetterOrDigit(str.charAt(i)) || Character.isSpaceChar(str.charAt(i))) {
        continue;
      }
      else {
        ret = false;
        break;
      }
    }
    return ret;
  }

  public static boolean isAlphaNumeric(String str) {
    boolean ret = true;

    for (int i = 0; i < str.length(); i++) {
      if (Character.isLetterOrDigit(str.charAt(i))) {
        continue;
      }
      else {
        ret = false;
        break;
      }
    }
    return ret;
  }

  public static boolean checkInvalidChar(String str) {
    boolean invalid = false;
    String att_to = EnsureNotNull(str);
    try {
      for (int j = 0; j < att_to.length(); j++) {
        // System.out.println((int)att_to.charAt(j));
        if (((int) att_to.charAt(j)) >= 128) {
          invalid = true;
          break;
        }
      }
    }
    catch (Throwable t) {
      // ignore
    }
    return (invalid);
  }

  public static String removeInvalidChar(String str) {
    String ret = "";
    String att_to = EnsureNotNull(str);
    try {
      for (int j = 0; j < att_to.length(); j++) {
        if (((int) att_to.charAt(j)) >= 128) {
          continue;
        }
        else {
          ret = ret + att_to.charAt(j);
        }
      }
    }
    catch (Throwable t) {
      // ignore
    }
    return (ret);
  }

  public static String removeSpaces(String str) {
    char[] newChars = new char[str.length()];
    int j = 0;

    for (int i = 0; i < str.length(); i++) {
      if (Character.isLetterOrDigit(str.charAt(i)))
        newChars[j++] = str.charAt(i);
    }

    return new String(newChars, 0, j);
  }

  public static String FillTrailingSpaces(String s, int totalLen) {
    StringBuffer sb = new StringBuffer(s);
    int additionalSpaces = totalLen - s.length();

    for (int i = 1; i <= additionalSpaces; i++) {
      sb.append(" ");
    }

    return sb.toString();
  }

  public static HashMap ToUpperCase(HashMap map) {
    HashMap uppedMap = new HashMap();
    Object key, value;
    String tmp;
    String[] tmpArr, tmpArr2;
    Iterator iter;
    Set keys;

    if (map != null) {
      keys = map.keySet();
      iter = keys.iterator();

      while (iter.hasNext()) {
        key = iter.next();
        value = map.get(key);

        if (value instanceof String) {
          tmp = (String) value;
          uppedMap.put(key, tmp.toUpperCase());
        }
        else if (value instanceof String[]) {
          tmpArr = (String[]) value;
          tmpArr2 = new String[tmpArr.length];

          for (int i = 0; i < tmpArr.length; i++) {
            if (tmpArr[i] != null) {
              tmpArr2[i] = tmpArr[i].toUpperCase();
            }
          }

          uppedMap.put(key, tmpArr2);
        }
        else {
          uppedMap.put(key, value);
        }
      } // End while iter.hasNext()
    } // End null map check

    return uppedMap;
  }

  /**
   * @param outputMode
   * @param output_modes
   * @return
   */
  public static boolean InList(String s, String[] list) {
    boolean inl = false;

    if (s != null && list != null && list.length > 0) {
      for (int i = 0; i < list.length; i++) {
        if (s.equals(list[i])) {
          inl = true;
          break;
        }
      }
    }

    return inl;
  }

  public static String SimpleHTMLCharEncoder(String text) {
    StringBuffer html = new StringBuffer();
    char c;

    if (text != null) {
      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);

        switch (c) {
          case '&':
            html.append("&amp;");
            break;
          case '<':
            html.append("&lt;");
            break;
          case '>':
            html.append("&gt;");
            break;
          case '\"':
            html.append("&quot;");
            break;
          default:
            html.append(c);
        }
      }
    }

    return html.toString();
  }

  public static String SimpleHTMLCharEncoderWithNewLineReplacement(String text) {
    StringBuffer html = new StringBuffer();
    char c;

    if (text != null) {
      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);

        switch (c) {
          case '&':
            html.append("&amp;");
            break;
          case '<':
            html.append("&lt;");
            break;
          case '>':
            html.append("&gt;");
            break;
          case '\"':
            html.append("&quot;");
            break;
          case '\n':
            html.append("<BR>");
            break;
          default:
            html.append(c);
        }
      }
    }

    return html.toString();
  }

  public static String ParseFilename(String filePath) {
    String filename = null;
    int dirIndex;

    if (filePath != null && filePath.trim().length() > 0) {
      filePath = filePath.trim();

      dirIndex = filePath.lastIndexOf("/");

      if (dirIndex < 0) {
        dirIndex = filePath.lastIndexOf("\\");
      }

      if (dirIndex >= 0 && filePath.length() > (dirIndex + 1)) {
        filename = filePath.substring(dirIndex + 1);
      }
    }

    return filename;
  }

  public static String StripFilenameExtension(String filename) {
    String baseFN = null;
    int extensionIndex;

    if (filename != null && filename.trim().length() > 0) {
      baseFN = filename.trim();

      extensionIndex = baseFN.lastIndexOf(".");
      if (extensionIndex >= 0) {
        baseFN = baseFN.substring(0, extensionIndex);
      }
    }

    return baseFN;
  }

  public static int GetNextArrayIndex(String[] arr, String val, int startIndex) {
    int index = -1;

    if (arr != null && val != null && startIndex >= 0 && startIndex < arr.length) {
      for (int i = startIndex; i < arr.length; i++) {
        if (val.equals(arr[i])) {
          index = i;
          break;
        }
      }
    }

    return index;
  }

  public static int[] ToIntArr(String[] strArr) {
    int[] intArr = null;

    if (strArr == null) {
      return intArr;
    }

    intArr = new int[strArr.length];

    for (int i = 0; i < strArr.length; i++) {
      intArr[i] = Integer.parseInt(strArr[i]);
    }

    return intArr;
  }

  public static boolean EqualArrays(String[] arr1, String[] arr2) {
    boolean same = false;

    if (arr1 != null && arr2 != null && arr1.length == arr2.length) {
      same = true;

      for (int i = 0; i < arr1.length; i++) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && !arr1[i].equals(arr2[i]))) {
          same = false;
        }
      }
    }

    return same;
  }

  public static String[] RemoveTrailingEmptyElements(String[] arr) {
    String[] trimmedArr = null;
    ArrayList<String> al;

    if (arr != null && arr.length > 0) {
      al = new ArrayList<String>();

      for (int i = 0; i < arr.length; i++) {
        if (arr[i] != null && arr[i].trim().length() > 0) {
          al.add(arr[i]);
        }
      }

      trimmedArr = new String[al.size()];
      trimmedArr = (String[]) al.toArray(trimmedArr);
    }

    return trimmedArr;
  }

  public static synchronized String GenerateCompactTimeUniqueId() {
    StringBuffer id = new StringBuffer();

    // Some Random Character
    for (int i = 1; i <= 4; i++) {
      id.append((char) (65 + ((int) (26 * Math.random()))));
    }

    id.append(Long.toString(System.currentTimeMillis(), Character.MAX_RADIX));

    try {
      Thread.sleep(10);
    }
    catch (Exception e) {}

    return id.toString();
  }

  public static int CountNonEmptyElements(String[] arr) {
    int cnt = 0;

    if (arr != null) {
      for (int i = 0; i < arr.length; i++) {
        if (arr[i] != null && arr[i].trim().length() > 0) {
          cnt++;
        }
      }
    }

    return cnt;
  }

  /**
   * Difference between this method and the QuoteSplit method is that the
   * QuoteSplit method expects ALL fields to be encasulated in quotes, whereas
   * this method will respect a quoted field but does not require all fields to
   * be in sets of quotes!
   * 
   * @param text
   * @param splitChar
   * @return
   */
  public static String[] SplitRespectQuotes(String text, char splitChar) {
    String[] tempArr = null;
    ArrayList<String> al;
    StringBuffer sb;
    int cnt;
    boolean inQuotes;
    char c;

    if (text != null) {
      al = new ArrayList<String>();
      sb = new StringBuffer();
      inQuotes = false;

      for (int i = 0; i < text.length(); i++) {
        c = text.charAt(i);
        if (c == splitChar && !inQuotes) {
          al.add(sb.toString());
          sb = new StringBuffer();
        }
        else if (c == '\"') {
          inQuotes = !inQuotes;
        }
        else {
          sb.append(c);
        }
      }

      if (sb.length() > 0) {
        al.add(sb.toString());
      }

      cnt = 0;
      tempArr = new String[al.size()];
      while (!al.isEmpty()) {
        tempArr[cnt++] = (String) al.remove(0);
      }
    } // End text != null check

    return tempArr;
  }

  public static String Combine(String[] arr, String spacer) {
    StringBuffer sb = new StringBuffer();

    if (arr != null) {
      for (int i = 0; i < arr.length; i++) {
        if (i > 0) {
          sb.append(spacer);
        }

        sb.append(arr[i]);
      }
    }

    return sb.toString();
  }

  public static String EnsureFilenameChars(String origName, String replacementChars) {
    String newName = origName;
    final String[] INVALID_FN_CHARS = new String[] { "\\*", ";", "/", "\\\\", "\"", "\\|", ">", "<", ":" };

    if (origName != null && replacementChars != null) {
      newName = newName.trim();

      for (int i = 0; i < INVALID_FN_CHARS.length; i++) {
        newName = newName.replaceAll(INVALID_FN_CHARS[i], replacementChars);
      }
    }

    return newName;
  }

  public static String ConvertEscapedUnicode(String escapedStr) throws UnsupportedEncodingException {
    String unicode = null, hexCh;
    String[] tmpArr, unicodeChars;
    int ascii;
    byte b;
    byte[] binary;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    if (escapedStr == null || escapedStr.toUpperCase().indexOf("\\U") == -1) {
      unicode = escapedStr;
    }
    else {
      tmpArr = escapedStr.trim().toUpperCase().split("\\\\U");

      // If string in tmpArr[0] is NOT empty... That means the string was
      // prefixed with a set of NON-international characters... Process it
      // separately!
      if (tmpArr[0].length() > 0) {
        for (int i = 0; i < tmpArr[0].length(); i++) {
          baos.write(0);
          baos.write(tmpArr[0].substring(i, i + 1).getBytes(), 0, 1);
        }
      }

      // Get rid of the empty string in tmpArr[0] caused by the \\U "delimiter"
      // prefix of the escaped string...
      unicodeChars = new String[tmpArr.length - 1];

      for (int i = 1; i < tmpArr.length; i++) {
        unicodeChars[i - 1] = tmpArr[i];
      }

      // Convert the unicode escaped chars to binary...
      for (int i = 0; i < unicodeChars.length; i++) {
        // First Byte
        hexCh = unicodeChars[i].substring(0, 2);
        ascii = Integer.parseInt(hexCh, 16);
        b = (byte) ((ascii & 0x000000ffL));

        baos.write(b);

        // Second Byte
        hexCh = unicodeChars[i].substring(2, 4);
        ascii = Integer.parseInt(hexCh, 16);
        b = (byte) ((ascii & 0x000000ffL));

        baos.write(b);

        if (unicodeChars[i].length() > 4) {
          for (int j = 4; j < unicodeChars[i].length(); j++) {
            baos.write(0);
            baos.write(unicodeChars[i].substring(j, j + 1).getBytes(), 0, 1);
          }
        }
      } // End for i loop through unicodeChars

      binary = baos.toByteArray();
      unicode = new String(binary, "UnicodeBig");
    } // End else block

    return unicode;
  }

  /*
   * To get value for a html tag. Parameters: response =
   * <TICKET_NUMBER>HD0000004420727</TICKET_NUMBER>, tag=TICKET_NUMBER return =
   * HD0000004420727
   */
  public static String getValueForTag(String response, String tag) {
    String ret = "";

    if (StringUtils.IsNVL(response) || StringUtils.IsNVL(tag))
      return "";

    int sTagInx = response.indexOf("<" + tag + ">");
    int eTagInx = response.indexOf("</" + tag + ">");
    if (sTagInx > -1 && eTagInx > -1) {
      ret = response.substring(sTagInx + tag.length() + 2, eTagInx);
    }
    return ret;
  }

  public static String TrimOrNull(String s) {
    if (s == null) {
      return s;
    }
    else {
      return s.trim();
    }
  }

  public static String[] StringOfLinesToArray(String lines) throws IOException {
    StringReader sr = null;
    BufferedReader br = null;
    ArrayList<String> al = null;
    String[] arr = null;
    String line;

    try {
      sr = new StringReader(lines);
      br = new BufferedReader(sr);

      al = new ArrayList<String>();

      line = br.readLine();

      while (line != null) {
        al.add(line);
        line = br.readLine();
      }

      arr = new String[al.size()];
      arr = al.toArray(arr);
    } //End try block
    finally {
      if (al != null) {
        al.clear();
      }

      if (br != null) {
        try {
          br.close();
        }
        catch (Exception e) {}
      }

      if (sr != null) {
        try {
          sr.close();
        }
        catch (Exception e) {}
      }
    }

    return arr;
  }

  public static String[] ParseLines(String str) throws IOException {
    StringReader sr = null;
    BufferedReader br = null;
    String[] lines = null;
    String line;
    ArrayList<String> al = null;
    int cnt;

    try {
      sr = new StringReader(str);
      br = new BufferedReader(sr);
      al = new ArrayList<String>();

      line = br.readLine();
      while (line != null) {
        if (line.trim().length() > 0) {
          al.add(line.trim());
        }
        line = br.readLine();
      }

      cnt = 0;
      lines = new String[al.size()];
      while (!al.isEmpty()) {
        lines[cnt++] = (String) al.remove(0);
      }
    }
    finally {
      if (br != null) {
        try {
          br.close();
        }
        catch (Exception e) {}
        br = null;
      }

      if (sr != null) {
        try {
          sr.close();
        }
        catch (Exception e) {}
        sr = null;
      }

      if (al != null) {
        al.clear();
        al = null;
      }
    }

    return lines;
  }

  public static String ReadLineFromUnixBinary(byte[] data) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    for (int i = 0; i < data.length; i++) {
      if (data[i] == 10) {
        break;
      }
      else {
        baos.write(data[i]);
      }
    }

    return baos.toString();
  }

  public static String ReadLineFromDosBinary(byte[] data) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    for (int i = 0; i < data.length; i++) {
      if (data[i] == 13) {
        break;
      }
      else {
        baos.write(data[i]);
      }
    }

    return baos.toString();
  }

  public static String ReadLineFromHttpBinary(byte[] data) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    for (int i = 0; i < data.length; i++) {
      if (data[i] == 13 && (((i + 1) < data.length) && data[i + 1] == 10)) {
        break;
      }
      else {
        baos.write(data[i]);
      }
    }

    return baos.toString();
  }

  public static boolean Equals(String str1, String str2) {
    return str1 == null ? str2 == null : str1.equals(str2);
  }

  public static String MD5Hashing(String original) throws NoSuchAlgorithmException {
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    md5.update(original.getBytes());
    return Base64Codec.Encode(md5.digest());
  }

  public static void AppendSpaces(StringBuffer buf, int cnt) {
    for (int i = 1; i <= cnt; i++) {
      buf.append(" ");
    }
  }

  public static String Combine(int[] arr, String spacer) {
    StringBuffer sb = new StringBuffer();

    if (arr != null) {
      for (int i = 0; i < arr.length; i++) {
        if (i > 0) {
          sb.append(spacer);
        }

        sb.append(arr[i]);
      }
    }

    return sb.toString();
  }

  public static String GetCurrentMethodName() {
    String callingMethod = null;

    StackTraceElement[] steArr = Thread.currentThread().getStackTrace();

    if (steArr.length >= 3) {
      callingMethod = steArr[2].getMethodName();
    }

    return callingMethod;
  }

  public static String GetCallingMethodName() {
    String callingMethod = null;

    StackTraceElement[] steArr = Thread.currentThread().getStackTrace();

    if (steArr.length >= 4) {
      callingMethod = steArr[3].getMethodName();
    }

    return callingMethod;
  }

  public static Properties GetPropertiesUsingPropNamePrefix(Properties props, String prefix) {
    Properties prefixedProps = null;
    Iterator<Object> iter;
    String name, value;

    if (props != null) {
      prefixedProps = new Properties();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        if (name.startsWith(prefix)) {
          prefixedProps.setProperty(name, value);
        }
      }
    }

    return prefixedProps;
  }

  public static Properties RemovePrefixFromPropNames(Properties props, String prefix) {
    Properties unprefixedProps = null;
    Iterator<Object> iter;
    String name, value;
    int beginIndex;

    if (props != null) {
      unprefixedProps = new Properties();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        if (name.startsWith(prefix)) {
          beginIndex = prefix.length();
          name = name.substring(beginIndex);
          unprefixedProps.setProperty(name, value);
        }
      }
    }

    return unprefixedProps;
  }

  public static List<String> GetValueListUsingPropNamePrefix(Properties props, String prefix) {
    ArrayList<String> valueLst = null;
    Iterator<Object> iter;
    String name, value;

    if (props != null) {
      valueLst = new ArrayList<String>();

      iter = props.keySet().iterator();

      while (iter.hasNext()) {
        name = (String) iter.next();
        value = props.getProperty(name);

        if (name.startsWith(prefix)) {
          valueLst.add(value);
        }
      }
    }

    return valueLst;
  }

  public static String filterNonUtf8Characters(String input) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (isUtf8Character(c)) {
        result.append(c);
      }
    }
    return result.toString();
  }

  public static boolean isUtf8Character(char c) {
    // We can use Character.isValidCodePoint to check if the code point is valid
    // and Character.isBmpCodePoint to check if it's in the Basic Multilingual Plane (BMP).
    int codePoint = (int) c;
    return Character.isValidCodePoint(codePoint) && Character.isBmpCodePoint(codePoint);
  }

}
