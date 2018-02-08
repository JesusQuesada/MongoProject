
package proyecto_mongo;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.bson.Document;

/**
 * 
 * @author jquesadaabeijon
 */
public class Proyecto_mongo {
      
    public static void main(String[] args) {
        
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE); 

        MongoClient cl = new MongoClient("localhost",27017); //Conexión
        MongoDatabase db = cl.getDatabase("training"); //Conexión con la base de datos
        MongoCollection <Document> coleccion = db.getCollection("scores"); //Conexión con la colección
        BasicDBObject condicion = new BasicDBObject("kind","essay"); //Condición para la consulta
        FindIterable <Document> cursor1 = coleccion.find(condicion); //Crea el cursor para iterar
        MongoCursor <Document> iterar = cursor1.iterator(); //Llama al cursor que hemos creado
      
            while(iterar.hasNext()){
                Document doc = iterar.next();
                String kind = doc.getString("kind");
                Double score = doc.getDouble("score");
                Double student = doc.getDouble("student");
                System.out.println(kind + " , " + score + " , " + student);                
            }
        
        /*
        Añade un nuevo documento.
        Para añadir un subdocumento, utilizamos "new Document() dentro del .append.
        */    
        Document engadir = new Document("kind", "taller");
        engadir.append("score", 111111);
        engadir.append("enderezo", new Document()
               .append("rua", "urzaiz")
               .append("numero", 27)
        );
        
        coleccion.insertOne(engadir);
        
        /*
        Actualiza uno o varios objetos: 
        1 --> updateOne 
        Varios --> updateMany
        */
        BasicDBObject condicion2 = new BasicDBObject("kind","proba");
        BasicDBObject oper = new BasicDBObject("$set",new BasicDBObject("numero", 20));        
        coleccion.updateMany(condicion2,oper);
        //Otra forma de hacer el update sería:
        //Document condicion3 = new Document("kind","proba");
        //Document opera = new Document("$set",new Document("numero", 20));
        //coleccion.updateMany(condicion3, opera);
        
        /*
        Incrementar un objeto:
        */
        BasicDBObject condicion4 = new BasicDBObject("kind","proba");
        BasicDBObject operar = new BasicDBObject("$inc",new BasicDBObject("numero", 10));
        coleccion.updateMany(condicion4, operar);
        
        /*
        Borrar un objeto o varios:
        1 --> deleteOne
        Varios --> deleteMany
        */
        BasicDBObject condicion5 = new BasicDBObject("kind","proba");
        coleccion.deleteOne(condicion5);
        
        /*
        Buscar por clave:
        */
        BasicDBObject condicion6 = new BasicDBObject("_id",1000);
        FindIterable <Document> cursor2 = coleccion.find(condicion6);
        Document doc = cursor2.first();
        String kind = doc.getString("kind");
        Integer numero = doc.getInteger("numero");
        System.out.println(kind + " , " + numero);
        
        iterar.close();
        cl.close();
    }
}
    

