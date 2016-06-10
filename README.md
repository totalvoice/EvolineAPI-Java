# EvolineAPI-Java
Lib Java para integração com a API Evoline

## Como utilizar (how to)

```java
import br.com.evoline.EvolineAPI;
import org.json.JSONObject;

public class Teste{
    public static void main(String a[]) {
		EvolineAPI api = new EvolineAPI("{[ Access-Token }}"); //disponível no painel do usuário da api

		api.debugOn(); //loga na console todos as requests e retornos

		api.consultaSaldo();

		api.enviaSMS("***********", "isto é um teste");
		
		JSONObject chamada = api.enviaChamada("********", "*********");

		JSONObject relatorio = api.relatorioChamadas("2016-06-01", "2016-06-30");
		
	}
}
```

## Pré requisitos

- JSONObject (https://github.com/stleary/JSON-java) (http://www.java2s.com/Code/Jar/j/Downloadjavajsonjar.htm)


## Licença

MIT
