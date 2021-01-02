/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.adc.auctionProject;

/**
 *
 * @author alfonso
 */
public class CreateAuctionException extends Exception{
  public CreateAuctionException() { super(); }
  public CreateAuctionException(String message) { super(message); }
  public CreateAuctionException(String message, Throwable cause) { super(message, cause); }
  public CreateAuctionException(Throwable cause) { super(cause); }
}
