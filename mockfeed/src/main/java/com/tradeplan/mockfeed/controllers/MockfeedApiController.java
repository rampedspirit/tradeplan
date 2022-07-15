package com.tradeplan.mockfeed.controllers;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import com.tradeplan.mockfeed.api.MockfeedApi;
import com.tradeplan.mockfeed.model.TokenRequest;
import com.tradeplan.mockfeed.model.TokenResponse;
import com.tradeplan.mockfeed.model.TokenResponse.TokenTypeEnum;
import com.tradeplan.mockfeed.persistence.UserEntity;
import com.tradeplan.mockfeed.service.SymbolService;
import com.tradeplan.mockfeed.service.UserService;

@Controller
@CrossOrigin
public class MockfeedApiController implements MockfeedApi {

	private DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd'T'HH:mm:ss");

	@Autowired
	private UserService userService;

	@Autowired
	private SymbolService symbolService;

	@Override
	public ResponseEntity<byte[]> getAllSymbols(@NotNull @Valid String segment, @NotNull @Valid String name,
			@NotNull @Valid String password, @NotNull @Valid Boolean csv, @NotNull @Valid Boolean allexpiry) {
		UserEntity user = userService.getUserByNameAndPassword(name, password);
		if (user != null) {
			byte[] symbols = symbolService.getAllSymbolsAsCSV("nse");
			return new ResponseEntity<byte[]>(symbols, HttpStatus.OK);
		}
		return new ResponseEntity<byte[]>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<byte[]> getBars(String bearer, @NotNull @Valid String symbol, @NotNull @Valid String from,
			@NotNull @Valid String to, @NotNull @Valid String response, @NotNull @Valid String interval) {
		UserEntity user = userService.getUserByToken(bearer);
		if (user != null) {
			OffsetDateTime fromTime = OffsetDateTime.of(LocalDateTime.parse(from, TIME_FORMATTER),
					ZoneOffset.of("+05:30"));
			OffsetDateTime toTime = OffsetDateTime.of(LocalDateTime.parse(to, TIME_FORMATTER),
					ZoneOffset.of("+05:30"));
			byte[] bars = symbolService.getBarsAsCSV(symbol, fromTime, toTime);
			return new ResponseEntity<byte[]>(bars, HttpStatus.OK);
		}
		return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<TokenResponse> getToken(@Valid TokenRequest tokenRequest) {
		String token = userService.createOrGetToken(tokenRequest.getUsername(), tokenRequest.getPassword());
		TokenResponse response = new TokenResponse();
		response.setAccessToken(token);
		response.setTokenType(TokenTypeEnum.BEARER);
		return new ResponseEntity<TokenResponse>(response, HttpStatus.OK);
	}
}
