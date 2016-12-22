/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package process;

import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import rdf.RDF;
import rdf.Sparql;
import utils.ReadIndex;

/**
 *
 * @author swalter
 */
public class Process {
    public static void main (String[] args) throws IOException {
        String path_to_index = args[0];
        String path_to_topic_list = args[1];

        String content = new String(Files.readAllBytes(Paths.get(path_to_topic_list)));
        String[] topics = content.split("\n");
        
        ReadIndex index = new ReadIndex(path_to_index);
        Sparql sparql = new Sparql();
        
        for(String t : topics){
            t = t.toLowerCase();
            List<String> terms = new ArrayList<>();
            if(t.contains(" ")){
                for(String x : t.split(" ")) terms.add(x);
            }
            else{
                terms.add(t);
            }
            List<String> lines = new ArrayList<>();
            for(String sentence :index.runSearch(terms)){
                Model default_model = ModelFactory.createDefaultModel();
                RDF.convertSentenceToRDF(default_model,sentence);
                List<List<String>> results = sparql.extract(default_model);
                for(List<String> result : results){
                    String subject_lemma = result.get(1);
                    if(terms.contains(subject_lemma)){
                        lines.add(t+"\t"+result.get(4)+"\t"+subject_lemma+"\t"+result.get(0)+"\t"+result.get(2)+"\t"+result.get(3)+"\t"+sentence);
                    }
                }
            }
             Files.write(Paths.get("topics.tsv"), lines, UTF_8, APPEND, CREATE);
        }
        
    }
}
