package buildclasse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import buildclasse.Fonctioncle;

public class BuildClasse {
    String chemintemplate ;
    String nombase;
    String jdbc;
    String nomtable;
    String numport;
    String nomuser;
    String password;
    java.util.List<String> template = new java.util.ArrayList<String>();
    java.util.List<String[]> colonne =new java.util.ArrayList<String[]>();
    int annoted=0;
    public void setTemplate(){
        template=this.getLigneHtlm(chemintemplate);
    }
    public BuildClasse(){

    }
    public BuildClasse(String chemin,int annoted,String nombase,String jdbc,String nomtable,String numport,String nomuser,String password){
            this.password=password;
            this.nomuser=nomuser;
            this.nombase=nombase;
            this.jdbc=jdbc;
            this.nomtable=nomtable;
            this.numport=numport;
            this.annoted=annoted;
            chemintemplate=chemin;
            this.setTemplate();
            colonne= new Description(nombase,jdbc).getDescriptionTable(nomtable);
    }
     public java.util.List<String> getLigneHtlm(String chemintemplate){
        java.util.List<String> template = new java.util.ArrayList<String>();
        try {
            FileReader fichierReader = new FileReader(chemintemplate);
            BufferedReader bufferedReader = new BufferedReader(fichierReader);
            String ligne;
            while ((ligne = bufferedReader.readLine()) != null) {
                System.out.println(ligne);
                template.add(ligne);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return template;
    }
    public void buildClassefile(){
        String nomFichier = Fonctioncle.mettrePremiereLettreEnMajuscule(nomtable);
        java.util.List<String> constructeur = Fonctioncle.doConstructeur(colonne, nomtable);
        java.util.List<String> syntaxeprimarykey = Fonctioncle.buildSyntaxe(Fonctioncle.getSyntaxetemplate("primaryKey","finprimaryKey",template) ,new String[][]{{"Type",colonne.get(0)[0]},{"Colonne",colonne.get(0)[1]},{"nomtable",nomtable}},0)  ;
        java.util.List<String> getteursetteurs = Fonctioncle.doConstructeur(colonne, nomtable);
        java.util.List<String> syntaxegetset = Fonctioncle.getSyntaxetemplate("debutC","finC",template);
        int p=0; 
        try {
            File fichier = new File(template.get(0).replace("nomtable", nomFichier));
            if (!fichier.exists()) {
                fichier.createNewFile();
            }
            if(template.size()>=0){
                template.remove(0);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(fichier, true)); // 'true' pour écrire à la fin du fichier
            int a=1;
            for (String string : template) {
                
                    if(string.contains("@")==true && annoted==1){
                        continue ;
                    }
                    if(string.contains("debut")==true){
                        System.out.println("string debut :"+string);
                        p=1;
                    }
                    else if(string.contains("fin")==true){
                        System.out.println("string fin :"+string);
                        p=0;
                        continue ;
                    }
                    if(string.contains("attribu")==true){
                        for(String ligneprimarykey : syntaxeprimarykey ){
                            p=1;
                            // System.out.println("primary :"+ligneprimarykey);
                            writer.write(ligneprimarykey);
                            writer.newLine();
                        }
                        for (String[] string1 :  colonne ) {
                            writer.write("          "+string1[0]+" "+string1[1]+";");
                            writer.newLine();
                        }  
                    }
                    else if(string.contains("debutC")==true){
                        p=1;
                        System.out.println("setget");
                        for (String[] stringa :  colonne ) {
                            for (String string1 :  Fonctioncle.buildSyntaxeSetteursGetteurs(syntaxegetset,stringa[1],stringa[0],1) ) {
                                writer.write("          "+string1);
                                writer.newLine();
                            }
                        }
                    }else{
                        if(p==0){
                            string = string.replace("nomtable", nomFichier);
                            string = string.replace("mintable", this.nomtable);
                            string = string.replace("nomT", this.nomtable);
                            string = string.replace("nbrC", Integer.toString(colonne.size()) );
                            string=string.replace("nomS",nomtable+"sequence");
                            string = string.replace("nomB", this.nombase);
                            string = string.replace("baseT", this.jdbc);
                            string = string.replace("nomU", this.nomuser);
                            string = string.replace("passW", this.password);
                            string = string.replace("porT", this.numport);
                            writer.write(string);
                            writer.newLine();
                            System.out.println("ligne template  ecrit:"+string);
                        }
                    }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}