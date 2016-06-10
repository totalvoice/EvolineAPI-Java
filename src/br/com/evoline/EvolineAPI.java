package br.com.evoline;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class EvolineAPI {

	private String accessToken;
	private boolean debug = false;

	public EvolineAPI(String accessToken) {
		this.accessToken = accessToken;
	}

	public void debugOn() {
		this.debug = true;
	}

	public void debugOff() {
		this.debug = false;
	}

	private JSONObject sendRequest(String path, String method) {
		return this.sendRequest(path, method, null);
	}

	private JSONObject sendRequest(String path, String method, String body) {
		try {

			if (this.debug) {
				System.out.println("Evoline Request: " + path + " Method: " + method + " Body: " + body);
			}

			URL url = new URL("https://api.evoline.com.br" + path);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

			con.setRequestMethod(method);
			con.setRequestProperty("Access-Token", accessToken);

			if (("POST".equals(method) || "PUT".equals(method) && body != null && !"".equals(body))) {
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(body);
				wr.flush();
				wr.close();
			}

			InputStream is;
			if (con.getResponseCode() >= 200 && con.getResponseCode() <= 299) {
				is = con.getInputStream();
			} else {
				is = con.getErrorStream();
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			if (this.debug) {
				System.out.println("Evoline Response: " + response);
			}

			return new JSONObject(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/* CONTA */

	public JSONObject consultaSaldo() {
		return this.sendRequest("/saldo", "GET");
	}

	public JSONObject minhaConta() {
		return this.sendRequest("/conta", "GET");
	}

	public JSONObject atualizaDadosConta(String nome, String login, String senha, String cpf_cnpj, String telefone) {
		JSONObject body = new JSONObject();
		try {
			if (nome != null) {
				body.put("nome", nome);
			}
			if (login != null) {
				body.put("login", login);
			}
			if (senha != null) {
				body.put("senha", senha);
			}
			if (cpf_cnpj != null) {
				body.put("cpf_cnpj", cpf_cnpj);
			}
			if (telefone != null) {
				body.put("telefone", telefone);
			}
		} catch (Exception e) {

		}

		return this.sendRequest("/conta", "PUT", body.toString());
	}

	/* CHAMADA */

	public JSONObject enviaChamada(String origem, String destino) {
		return enviaChamada(origem, destino, false, null, null);
	}

	public JSONObject enviaChamada(String origem, String destino, boolean gravar_audio) {
		return enviaChamada(origem, destino, gravar_audio, null, null);
	}

	public JSONObject enviaChamada(String origem, String destino, boolean gravar_audio, String bina_origem,
			String bina_destino) {
		JSONObject body = new JSONObject();
		try {
			body.put("numero_origem", origem);
			body.put("numero_destino", destino);
			body.put("gravar_audio", gravar_audio);
			if (bina_origem != null) {
				body.put("bina_origem", bina_origem);
			}
			if (bina_destino != null) {
				body.put("bina_destino", bina_destino);
			}
		} catch (Exception e) {

		}

		return this.sendRequest("/chamada", "POST", body.toString());
	}

	public JSONObject cancelaChamada(int chamadaId) {
		return this.sendRequest("/chamada/" + chamadaId, "DELETE");
	}

	public JSONObject statusChamada(int chamadaId) {
		return this.sendRequest("/chamada/" + chamadaId, "GET");
	}

	public JSONObject relatorioChamadas(String dataInicio, String dataFim) {
		return this.sendRequest("/chamada/relatorio?data_inicio=" + dataInicio + "&data_fim=" + dataFim, "GET");
	}

	/* SMS */

	public JSONObject enviaSMS(String numero_destino, String mensagem) {
		JSONObject body = new JSONObject();
		try {
			body.put("numero_destino", numero_destino);
			body.put("mensagem", mensagem);
		} catch (Exception e) {

		}

		return this.sendRequest("/sms", "POST", body.toString());
	}

	public JSONObject statusSMS(int smsId) {
		return this.sendRequest("/sms/" + smsId, "GET");
	}

	public JSONObject relatorioSMS(String dataInicio, String dataFim) {
		return this.sendRequest("/sms/relatorio?data_inicio=" + dataInicio + "&data_fim=" + dataFim, "GET");
	}

	/* TTS */

	public JSONObject enviaTTS(String numero_destino, String mensagem) {
		return this.enviaTTS(numero_destino, mensagem, 0, false);
	}

	public JSONObject enviaTTS(String numero_destino, String mensagem, int velocidade, boolean resposta_usuario) {
		JSONObject body = new JSONObject();
		try {
			body.put("numero_destino", numero_destino);
			body.put("mensagem", mensagem);
			body.put("velocidade", velocidade);
			body.put("resposta_usuario", resposta_usuario);
		} catch (Exception e) {

		}

		return this.sendRequest("/tts", "POST", body.toString());
	}

	public JSONObject statusTTS(int ttsIs) {
		return this.sendRequest("/tts/" + ttsIs, "GET");
	}

	public JSONObject relatorioTTS(String dataInicio, String dataFim) {
		return this.sendRequest("/tts/relatorio?data_inicio=" + dataInicio + "&data_fim=" + dataFim, "GET");
	}

	/* Audio */

	public JSONObject enviaAudio(String numero_destino, String url_audio) {
		return this.enviaAudio(numero_destino, url_audio, false);
	}

	public JSONObject enviaAudio(String numero_destino, String url_audio, boolean resposta_usuario) {
		JSONObject body = new JSONObject();
		try {
			body.put("numero_destino", numero_destino);
			body.put("url_audio", url_audio);
			body.put("resposta_usuario", resposta_usuario);
		} catch (Exception e) {

		}

		return this.sendRequest("/audio", "POST", body.toString());
	}

	public JSONObject statusAudio(int audioId) {
		return this.sendRequest("/audio/" + audioId, "GET");
	}

	public JSONObject relatorioAudio(String dataInicio, String dataFim) {
		return this.sendRequest("/audio/relatorio?data_inicio=" + dataInicio + "&data_fim=" + dataFim, "GET");
	}


}
