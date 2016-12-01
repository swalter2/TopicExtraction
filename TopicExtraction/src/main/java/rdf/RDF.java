package rdf;


import org.apache.jena.rdf.model.Model;

import org.apache.jena.rdf.model.Resource;

public class RDF {
	
	
    public static void convertSentenceToRDF(Model default_model,String input_sentence){
        int counter = 1;
        String class_token = "class"+Integer.toString(counter);

        Resource res_class_token = default_model.createResource("class:"+class_token);

        int word_number = 0;
        for (String item:input_sentence.split("<n>")){
            word_number ++;
            String[] x = item.split("<t>");
            if (x.length > 1){
                try{
                    String id = x[0].replace(" ", "");
                    String head_id = x[6].replace(" ", ""); 
                    String token = "token"+Integer.toString(counter)+"_"+id;
                    String head_token = "token"+Integer.toString(counter)+"_"+head_id;

                    Resource row_subject = default_model.createResource("token:"+token)
                                    .addProperty(default_model.createProperty("own:partOf"), res_class_token)
                                    .addProperty(default_model.createProperty("conll:wordnumber"), Integer.toString(word_number));


                    String form = x[1].toLowerCase().replace("\"","");
                    form = form.replace("(", "");
                    form = form.replace(")", "");
                    form = form.replace(" %", "");
                    row_subject.addProperty(default_model.createProperty("conll:form"), form);



                    if(!x[2].equals("_")){
                            row_subject.addProperty(default_model.createProperty("conll:lemma"), x[2].toLowerCase());
                    }


                    row_subject.addProperty(default_model.createProperty("conll:cpostag"), x[3]);

                    row_subject.addProperty(default_model.createProperty("conll:postag"), x[4]);

                    row_subject.addProperty(default_model.createProperty("conll:feats"), x[5]);

                    row_subject.addProperty(default_model.createProperty("conll:head"), default_model.createResource("token:"+head_token));

                    row_subject.addProperty(default_model.createProperty("conll:deprel"), x[7].replace(" ", ""));
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            }			

        }

    }


}
