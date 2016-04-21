package com.wangdao.mutilword.utils;


import com.wangdao.mutilword.bean.interpretBean.Dict;
import com.wangdao.mutilword.bean.interpretBean.Sent;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MonkeyzZi on 2016/4/20.
 */
public class SaxParseXml extends DefaultHandler{
    private Dict dict;
    private List<Sent> sents=new ArrayList<Sent>();
    private Sent sent=null;
    //用来存放每次遍历后的元素名称(节点名称)
    private String tagName;

    public Dict getDict() {
        return dict;
    }


    public void setDict(Dict dict) {
        this.dict = dict;
    }

   private StringBuilder sb = new  StringBuilder();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        sb.append(ch, start, length);

    }

    //只调用一次  初始化list集合
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        sents = new ArrayList<Sent>();

    }


    //调用多次    开始解析
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        sb.setLength(0);
        if(qName.equals("sent")){
            sent=new Sent();

        }else if(qName.equals("dict")){
            dict=new Dict();
        }
        this.tagName=qName;
    }


    //调用多次
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        super.endElement(uri, localName, qName);

        String data = sb.toString();
        if(qName.equals("sent")){
            this.sents.add(sent);
            //sent=null;
        } else if(qName.equals("dict")){
            dict.setSents(sents);

        }
        if(this.tagName!=null){
            if(this.tagName.equals("key")){
                dict.setKey(data);
            }
            else if(this.tagName.equals("ps")){
                dict.setPs(data);
            }
            else if(this.tagName.equals("pron")){
                dict.setPron(data);
            }
            else if(this.tagName.equals("pos")){
                dict.setPos(data);
                //System.out.println("pos----------"+new String(ch));
            }
            else if(this.tagName.equals("acceptation")){
                dict.setAcceptation(data) ;
            }
            else if(this.tagName.equals("orig")){
                sent.setOrig(data);
            }
            else if(this.tagName.equals("pron")){
                sent.setPronUrl(data);
            }
            else if(this.tagName.equals("trans")){
                sent.setTrans(data);
            }
        }

    }


    //只调用一次
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    //调用多次
  /*  @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if(this.tagName!=null){
            String data=new String(ch,start,length);
            if(this.tagName.equals("key")){
                dict.setKey(data);
            }
            else if(this.tagName.equals("ps")){
                dict.setPs(data);
            }
            else if(this.tagName.equals("pron")){
                dict.setPron(data);
            }
            else if(this.tagName.equals("pos")){
                dict.setPos(data);
                System.out.println("pos----------"+new String(ch));
            }
            else if(this.tagName.equals("acceptation")){
                dict.setAcceptation(data) ;
            }
            else if(this.tagName.equals("orig")){
                sent.setOrig(data);
            }
            else if(this.tagName.equals("pron")){
                sent.setPronUrl(data);
            }
            else if(this.tagName.equals("trans")){
                sent.setTrans(data);
            }
        }
        tagName=null ;
    }*/
}
