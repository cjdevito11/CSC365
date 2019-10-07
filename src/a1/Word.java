/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

/**
 *
 * @author CJ
 */
public class Word {
    
    private int rawFreq;
    private double tf;
    private double tfIdf;
    
    public Word() { rawFreq = 1; }
    
    public int getRawFreq()  { return rawFreq; }
    
    public void incRawFreq() { rawFreq++; }
    
    public double getTf()    { return tf; }
    public double getTfIdf() { return tfIdf; }
    
    public void setTf(double inputTf)       { tf = inputTf; }
    public void setTfIdf(double inputTfIdf) { tfIdf = inputTfIdf; }
   
}
