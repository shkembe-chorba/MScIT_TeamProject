<html>

<#include "./assets/ftl-templates/Head.ftl">

	<body>
		<div class="container">
			<#include "./assets/ftl-templates/Nav.ftl">

				<div class="jumbotron jumbotron-fluid">
					<div class="container">
						<div class="row justify-content-md-center">
							<div class="p-4">
								<h1> Top Trumps </h1>
							</div>
						</div>
						<div class="row justify-content-md-center ">
							<div class="col-auto p-3">
								<a class="btn btn-outline-primary btn-lg" href="/toptrumps/game" role="button">Game</a>
							</div>
						</div>
						<div class="row justify-content-md-center">
							<div class="col-auto">
								<a class="btn btn-outline-primary btn-lg" href="/toptrumps/stats" role="button">Statistics</a>
							</div>
						</div>

					</div>
				</div>
		</div>
	</body>

</html>